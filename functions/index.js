const { setGlobalOptions } = require("firebase-functions");
const { onCall, HttpsError } = require("firebase-functions/v2/https");
const { onRequest } = require("firebase-functions/v2/https");
const { onSchedule } = require("firebase-functions/v2/scheduler");
const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const { initializeApp } = require("firebase-admin/app");
const { getFirestore, FieldValue, Timestamp } = require("firebase-admin/firestore");
const { getMessaging } = require("firebase-admin/messaging");
const logger = require("firebase-functions/logger");
const { MercadoPagoConfig, Preference, Payment, PaymentRefund } = require("mercadopago");

initializeApp();
setGlobalOptions({ maxInstances: 10, region: "us-central1" });

const db = getFirestore();
const mpClient = new MercadoPagoConfig({ accessToken: process.env.MP_ACCESS_TOKEN });

// ============================================================
// 2a. crearPreferenciaPago — HTTPS Callable
// ============================================================
exports.crearPreferenciaPago = onCall(async (request) => {
  const { pedidoId } = request.data;
  if (!pedidoId) throw new HttpsError("invalid-argument", "pedidoId es requerido");

  const pedidoRef = db.collection("pedidos").doc(pedidoId);
  const snap = await pedidoRef.get();
  if (!snap.exists) throw new HttpsError("not-found", "Pedido no encontrado");

  const pedido = snap.data();

  const preference = new Preference(mpClient);
  const items = pedido.items.map((item) => ({
    title: item.nombre,
    quantity: item.cantidad,
    unit_price: item.precioUnitario,
    currency_id: "ARS",
  }));

  // Si hay costos calculados, usar el total como un solo item
  if (pedido.costos && pedido.costos.total) {
    items.length = 0;
    items.push({
      title: `Pedido Mía Bisutería - ${pedido.cliente.nombre}`,
      quantity: 1,
      unit_price: pedido.costos.total,
      currency_id: "ARS",
    });
  }

  const result = await preference.create({
    body: {
      items,
      back_urls: {
        success: "https://DOMINIO/pago-exitoso.html",
        failure: "https://DOMINIO/pago-fallido.html",
        pending: "https://DOMINIO/pago-exitoso.html",
      },
      auto_return: "approved",
      notification_url: "https://jacqueline-uncoaxial-chaffingly.ngrok-free.dev/mia-bisuteria/us-central1/webhookMercadoPago",
      external_reference: pedidoId,
    },
  });

  await pedidoRef.update({
    "pago.preferenceId": result.id,
    "pago.linkPago": result.init_point,
    actualizadoEn: FieldValue.serverTimestamp(),
  });

  return { linkPago: result.init_point, preferenceId: result.id };
});

// ============================================================
// 2b. webhookMercadoPago — HTTPS Request (no callable)
// WEBHOOK_URL: copiar esta URL al dashboard de MercadoPago en Notificaciones IPN
// ============================================================
exports.webhookMercadoPago = onRequest(async (req, res) => {
  if (req.method !== "POST") {
    res.status(200).send("OK");
    return;
  }

  try {
    const { type, data } = req.body;

    if (type === "payment" && data && data.id) {
      const payment = new Payment(mpClient);
      const paymentData = await payment.get({ id: data.id });
      const pedidoId = paymentData.external_reference;

      if (!pedidoId) {
        res.status(200).send("OK");
        return;
      }

      const pedidoRef = db.collection("pedidos").doc(pedidoId);
      const pedidoSnap = await pedidoRef.get();

      if (paymentData.status === "approved") {
        await pedidoRef.update({
          estado: "pago_confirmado",
          "pago.paymentId": String(data.id),
          "pago.estado": "aprobado",
          "pago.fechaPago": FieldValue.serverTimestamp(),
          actualizadoEn: FieldValue.serverTimestamp(),
        });

        // Enviar push notification a Mía
        const pedido = pedidoSnap.data();
        await enviarPushAdmin(
          "💳 Pago confirmado",
          `${pedido.cliente?.nombre || "Cliente"} pagó $${pedido.costos?.total?.toLocaleString("es-AR") || ""}`
        );
      } else if (paymentData.status === "rejected" || paymentData.status === "cancelled") {
        await pedidoRef.update({
          estado: "cancelado",
          "pago.paymentId": String(data.id),
          "pago.estado": "rechazado",
          actualizadoEn: FieldValue.serverTimestamp(),
        });
      }
    }
  } catch (err) {
    logger.error("Error procesando webhook:", err);
  }

  res.status(200).send("OK");
});

// ============================================================
// 2c. cancelarPedidosVencidos — Scheduled (cada 1 hora)
// ============================================================
exports.cancelarPedidosVencidos = onSchedule("every 60 minutes", async () => {
  const ahora = Timestamp.now();

  const vencidos = await db
    .collection("pedidos")
    .where("estado", "==", "pendiente_pago")
    .where("expiraEn", "<=", ahora)
    .get();

  if (vencidos.empty) {
    logger.info("No hay pedidos vencidos");
    return;
  }

  const batch = db.batch();
  vencidos.docs.forEach((doc) => {
    batch.update(doc.ref, {
      estado: "cancelado",
      actualizadoEn: FieldValue.serverTimestamp(),
    });
  });
  await batch.commit();

  logger.info(`Cancelados ${vencidos.size} pedidos vencidos`);
});

// ============================================================
// 2d. reembolsarPedido — HTTPS Callable
// ============================================================
exports.reembolsarPedido = onCall(async (request) => {
  const { pedidoId } = request.data;
  if (!pedidoId) throw new HttpsError("invalid-argument", "pedidoId es requerido");

  const pedidoRef = db.collection("pedidos").doc(pedidoId);
  const snap = await pedidoRef.get();
  if (!snap.exists) throw new HttpsError("not-found", "Pedido no encontrado");

  const pedido = snap.data();
  if (!pedido.pago || !pedido.pago.paymentId) {
    throw new HttpsError("failed-precondition", "El pedido no tiene un pago asociado");
  }

  const refund = new PaymentRefund(mpClient);
  await refund.create({ payment_id: Number(pedido.pago.paymentId) });

  await pedidoRef.update({
    estado: "cancelado",
    "pago.estado": "rechazado",
    actualizadoEn: FieldValue.serverTimestamp(),
  });

  logger.info(`Pedido ${pedidoId} reembolsado`);
  return { success: true, message: "Reembolso procesado correctamente" };
});

// ============================================================
// procesarPago — HTTPS Callable
// Recibe el token del CardPaymentBrick y cobra directamente
// ============================================================
exports.procesarPago = onCall(async (request) => {
  const { pedidoId, formData, email, nombre } = request.data;
  if (!pedidoId || !formData) throw new HttpsError("invalid-argument", "Datos incompletos");

  const pedidoRef = db.collection("pedidos").doc(pedidoId);
  const snap = await pedidoRef.get();
  if (!snap.exists) throw new HttpsError("not-found", "Pedido no encontrado");
  const pedido = snap.data();

  const payment = new Payment(mpClient);
  const result = await payment.create({
    body: {
      transaction_amount: pedido.costos.total,
      token:              formData.token,
      installments:       formData.installments || 1,
      payment_method_id:  formData.payment_method_id,
      issuer_id:          formData.issuer_id,
      payer: {
        email: email || "cliente@miabisuteria.ar",
        first_name: nombre || "",
      },
      external_reference: pedidoId,
      notification_url: "https://jacqueline-uncoaxial-chaffingly.ngrok-free.dev/mia-bisuteria/us-central1/webhookMercadoPago",
      description: `Pedido Mía Bisutería - ${nombre || pedido.cliente.nombre}`,
    },
  });

  if (result.status === "approved") {
    await pedidoRef.update({
      estado: "pago_confirmado",
      "pago.paymentId": String(result.id),
      "pago.estado":    "aprobado",
      "pago.fechaPago": FieldValue.serverTimestamp(),
      actualizadoEn:    FieldValue.serverTimestamp(),
    });
    await enviarPushAdmin(
      "💳 Pago confirmado",
      `${pedido.cliente.nombre} pagó $${pedido.costos.total.toLocaleString("es-AR")}`
    );
  } else {
    await pedidoRef.update({
      "pago.estado":  result.status,
      actualizadoEn: FieldValue.serverTimestamp(),
    });
  }

  return { status: result.status, paymentId: result.id };
});

// ============================================================
// Helper: enviar push a Mía usando token guardado en Firestore
// ============================================================
async function enviarPushAdmin(title, body) {
  try {
    const configSnap = await db.collection("config").doc("admin").get();
    const fcmToken = configSnap.exists ? configSnap.data().fcmToken : null;
    if (!fcmToken) { logger.warn("No hay FCM token guardado para admin"); return; }

    await getMessaging().send({
      token: fcmToken,
      notification: { title, body },
      webpush: { fcmOptions: { link: "/admin-app/pedidos.html" } },
    });
    logger.info(`Push enviado: ${title}`);
  } catch (err) {
    logger.warn("Error enviando push:", err.message);
  }
}

// ============================================================
// notificarNuevoPedido — Firestore trigger (onCreate)
// Se dispara cada vez que se crea un nuevo pedido
// ============================================================
exports.notificarNuevoPedido = onDocumentCreated("pedidos/{pedidoId}", async (event) => {
  const pedido = event.data.data();
  const nombre = pedido.cliente?.nombre || "Alguien";
  const tipo   = pedido.tipo === "personalizado" ? "personalizado" : "del catálogo";
  const total  = pedido.costos?.total
    ? ` — $${Number(pedido.costos.total).toLocaleString("es-AR")} ARS`
    : "";

  await enviarPushAdmin(
    "¡Nuevo pedido! 🌿",
    `${nombre} hizo un pedido ${tipo}${total}`
  );
});
