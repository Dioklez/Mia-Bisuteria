package com.miabisuteri.admin.data.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.miabisuteri.admin.domain.model.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    // ─── Productos ────────────────────────────────────────────────────────────

    suspend fun getProductos(): List<Producto> {
        val snap = db.collection("config").document("productos").get().await()
        val items = snap.get("items") as? List<Map<String, Any>> ?: emptyList()
        if (items.isEmpty()) return PRODUCTOS_DEFAULT
        return items.map { it.toProducto() }
    }

    suspend fun saveProductos(productos: List<Producto>) {
        val items = productos.map { it.toMap() }
        db.collection("config").document("productos")
            .set(mapOf("items" to items))
            .await()
    }

    // ─── Config del sitio ─────────────────────────────────────────────────────

    suspend fun getConfigSite(): ConfigSite {
        val snap = db.collection("config").document("site").get().await()
        if (!snap.exists()) return ConfigSite()
        return snap.toConfigSite()
    }

    suspend fun saveConfigSite(config: ConfigSite) {
        db.collection("config").document("site")
            .set(config.toMap())
            .await()
    }

    // ─── Pedidos (real-time) ──────────────────────────────────────────────────

    fun observePedidos(): Flow<List<Pedido>> = callbackFlow {
        val reg: ListenerRegistration = db.collection("pedidos")
            .orderBy("creadoEn", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val pedidos = snapshot?.documents?.mapNotNull { doc ->
                    runCatching { doc.toObject(HashMap::class.java)?.let {
                        (it as Map<String, Any>).toPedido(doc.id)
                    }}.getOrNull()
                } ?: emptyList()
                trySend(pedidos)
            }
        awaitClose { reg.remove() }
    }

    suspend fun getPedido(id: String): Pedido? {
        val doc = db.collection("pedidos").document(id).get().await()
        if (!doc.exists()) return null
        val data = doc.data ?: return null
        return data.toPedido(doc.id)
    }

    suspend fun updateEstadoPedido(id: String, estado: EstadoPedido) {
        db.collection("pedidos").document(id).update(
            mapOf(
                "estado" to estado.key,
                "actualizadoEn" to Timestamp.now()
            )
        ).await()
    }

    suspend fun updateCostosPedido(id: String, costos: CostosPedido) {
        db.collection("pedidos").document(id).update(
            mapOf(
                "costos" to costos.toMap(),
                "actualizadoEn" to Timestamp.now()
            )
        ).await()
    }

    // ─── Mappers ──────────────────────────────────────────────────────────────

    @Suppress("UNCHECKED_CAST")
    private fun Map<String, Any>.toProducto(): Producto {
        return Producto(
            id = (get("id") as? Number)?.toInt() ?: 0,
            nombre = get("nombre") as? String ?: "",
            precio = (get("precio") as? Number)?.toLong() ?: 0L,
            tipo = get("tipo") as? String ?: "pulsera",
            col = get("col") as? String ?: "flores",
            desc = get("desc") as? String ?: "",
            imgs = (get("imgs") as? List<*>)?.filterIsInstance<String>() ?: emptyList()
        )
    }

    private fun Producto.toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "nombre" to nombre,
        "precio" to precio,
        "tipo" to tipo,
        "col" to col,
        "desc" to desc,
        "imgs" to imgs
    )

    @Suppress("UNCHECKED_CAST")
    private fun Map<String, Any>.toPedido(docId: String): Pedido {
        val clienteMap = get("cliente") as? Map<String, Any> ?: emptyMap()
        val itemsList = (get("items") as? List<Map<String, Any>>) ?: emptyList()
        val personalMap = get("personalizado") as? Map<String, Any>
        val costosMap = get("costos") as? Map<String, Any>
        val pagoMap = get("pago") as? Map<String, Any>

        return Pedido(
            id = docId,
            tipo = get("tipo") as? String ?: "catalogo",
            estado = EstadoPedido.fromKey(get("estado") as? String ?: ""),
            cliente = ClienteInfo(
                nombre = clienteMap["nombre"] as? String ?: "",
                email = clienteMap["email"] as? String ?: "",
                telefono = clienteMap["telefono"] as? String ?: ""
            ),
            items = itemsList.map { item ->
                ItemPedido(
                    productoId = (item["productoId"] as? Number)?.toInt() ?: 0,
                    nombre = item["nombre"] as? String ?: "",
                    cantidad = (item["cantidad"] as? Number)?.toInt() ?: 1,
                    precioUnitario = (item["precioUnitario"] as? Number)?.toLong() ?: 0L
                )
            },
            personalizado = personalMap?.let {
                PersonalizadoInfo(
                    descripcion = it["descripcion"] as? String ?: "",
                    referencia = it["referencia"] as? String ?: "",
                    tipo = it["tipo"] as? String ?: "",
                    colores = it["colores"] as? String ?: ""
                )
            },
            costos = costosMap?.let {
                CostosPedido(
                    materiales = (it["materiales"] as? Number)?.toDouble() ?: 0.0,
                    tiempoHs = (it["tiempoHs"] as? Number)?.toDouble() ?: 0.0,
                    margenPct = (it["margenPct"] as? Number)?.toInt() ?: 40,
                    subtotal = (it["subtotal"] as? Number)?.toLong() ?: 0L,
                    envio = (it["envio"] as? Number)?.toDouble() ?: 0.0,
                    total = (it["total"] as? Number)?.toLong() ?: 0L
                )
            },
            pago = pagoMap?.let {
                PagoInfo(
                    preferenceId = it["preferenceId"] as? String ?: "",
                    paymentId = it["paymentId"] as? String ?: "",
                    estado = it["estado"] as? String ?: "pendiente",
                    linkPago = it["linkPago"] as? String ?: ""
                )
            },
            creadoEn = get("creadoEn") as? Timestamp,
            actualizadoEn = get("actualizadoEn") as? Timestamp
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun com.google.firebase.firestore.DocumentSnapshot.toConfigSite(): ConfigSite {
        val data = this.data ?: return ConfigSite()
        val destacadoMap = data["destacado"] as? Map<String, Any> ?: emptyMap()
        val favoritos = (data["favoritos"] as? List<*>)
            ?.filterIsInstance<Number>()?.map { it.toInt() } ?: emptyList()
        val galeria = (data["galeria"] as? List<*>)
            ?.filterIsInstance<String>() ?: emptyList()
        val testimoniosList = (data["testimonios"] as? List<Map<String, Any>>) ?: emptyList()
        val coleccionesList = (data["colecciones"] as? List<Map<String, Any>>) ?: emptyList()

        return ConfigSite(
            destacado = ProductoDestacado(
                nombre = destacadoMap["nombre"] as? String ?: "",
                precio = (destacadoMap["precio"] as? Number)?.toLong() ?: 0L,
                img = destacadoMap["img"] as? String ?: ""
            ),
            favoritos = favoritos,
            galeria = galeria,
            testimonios = testimoniosList.map { t ->
                Testimonio(
                    id = t["id"] as? String ?: "",
                    nombre = t["nombre"] as? String ?: "",
                    ciudad = t["ciudad"] as? String ?: "",
                    estrellas = (t["estrellas"] as? Number)?.toInt() ?: 5,
                    texto = t["texto"] as? String ?: ""
                )
            },
            colecciones = coleccionesList.map { c ->
                Coleccion(
                    key = c["key"] as? String ?: "",
                    emoji = c["emoji"] as? String ?: "",
                    nombre = c["nombre"] as? String ?: "",
                    img = c["img"] as? String ?: ""
                )
            }
        )
    }

    private fun ConfigSite.toMap(): Map<String, Any> = mapOf(
        "destacado" to mapOf(
            "nombre" to destacado.nombre,
            "precio" to destacado.precio,
            "img" to destacado.img
        ),
        "favoritos" to favoritos,
        "galeria" to galeria,
        "testimonios" to testimonios.map { t ->
            mapOf(
                "id" to t.id,
                "nombre" to t.nombre,
                "ciudad" to t.ciudad,
                "estrellas" to t.estrellas,
                "texto" to t.texto
            )
        },
        "colecciones" to colecciones.map { c ->
            mapOf("key" to c.key, "emoji" to c.emoji, "nombre" to c.nombre, "img" to c.img)
        }
    )

    private fun CostosPedido.toMap(): Map<String, Any> = mapOf(
        "materiales" to materiales,
        "tiempoHs" to tiempoHs,
        "margenPct" to margenPct,
        "subtotal" to subtotal,
        "envio" to envio,
        "total" to total
    )
}
