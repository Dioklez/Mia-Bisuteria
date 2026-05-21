package com.miabisuteri.admin.domain.model

import com.google.firebase.Timestamp

// ─── Producto ────────────────────────────────────────────────────────────────

data class Producto(
    val id: Int = 0,
    val nombre: String = "",
    val precio: Long = 0L,
    val tipo: String = "pulsera",
    val col: String = "flores",
    val desc: String = "",
    val imgs: List<String> = emptyList()
)

val TIPOS_PRODUCTO = listOf("pulsera", "argolla", "collar", "anillo", "tobillera")
val COLECCIONES_PRODUCTO = listOf("flores", "mariposa", "girasol", "cherry", "caracoles")

// ─── Pedido ───────────────────────────────────────────────────────────────────

data class Pedido(
    val id: String = "",
    val tipo: String = "catalogo", // "catalogo" | "personalizado"
    val estado: EstadoPedido = EstadoPedido.PENDIENTE_PAGO,
    val cliente: ClienteInfo = ClienteInfo(),
    val items: List<ItemPedido> = emptyList(),
    val personalizado: PersonalizadoInfo? = null,
    val costos: CostosPedido? = null,
    val pago: PagoInfo? = null,
    val creadoEn: Timestamp? = null,
    val actualizadoEn: Timestamp? = null
)

enum class EstadoPedido(val key: String, val display: String) {
    PENDIENTE_PAGO("pendiente_pago", "Pendiente pago"),
    PAGO_CONFIRMADO("pago_confirmado", "Pago confirmado"),
    EN_PROCESO("en_proceso", "En proceso"),
    LISTO("listo", "Listo para entregar"),
    CANCELADO("cancelado", "Cancelado");

    companion object {
        fun fromKey(key: String) = entries.find { it.key == key } ?: PENDIENTE_PAGO
    }
}

data class ClienteInfo(
    val nombre: String = "",
    val email: String = "",
    val telefono: String = ""
)

data class ItemPedido(
    val productoId: Int = 0,
    val nombre: String = "",
    val cantidad: Int = 1,
    val precioUnitario: Long = 0L
)

data class PersonalizadoInfo(
    val descripcion: String = "",
    val referencia: String = "",
    val tipo: String = "",
    val colores: String = ""
)

data class CostosPedido(
    val materiales: Double = 0.0,
    val tiempoHs: Double = 0.0,
    val margenPct: Int = 40,
    val subtotal: Long = 0L,
    val envio: Double = 0.0,
    val total: Long = 0L
)

data class PagoInfo(
    val preferenceId: String = "",
    val paymentId: String = "",
    val estado: String = "pendiente",
    val linkPago: String = ""
)

// ─── Calculadora ──────────────────────────────────────────────────────────────

data class Material(
    val nombre: String = "",
    val costo: Double = 0.0
)

data class CalculoResultado(
    val totalMateriales: Double = 0.0,
    val costoTiempo: Double = 0.0,
    val margenMonto: Double = 0.0,
    val costoEnvio: Double = 0.0,
    val total: Double = 0.0
)

// ─── Configuración del sitio ──────────────────────────────────────────────────

data class Coleccion(
    val key: String = "",
    val emoji: String = "",
    val nombre: String = "",
    val img: String = ""
)

data class Testimonio(
    val id: String = "",
    val nombre: String = "",
    val ciudad: String = "",
    val estrellas: Int = 5,
    val texto: String = ""
)

data class ConfigSite(
    val destacado: ProductoDestacado = ProductoDestacado(),
    val favoritos: List<Int> = emptyList(),
    val testimonios: List<Testimonio> = emptyList(),
    val colecciones: List<Coleccion> = emptyList(),
    val galeria: List<String> = emptyList()
)

data class ProductoDestacado(
    val nombre: String = "",
    val precio: Long = 0L,
    val img: String = ""
)

// ─── Auto-update ──────────────────────────────────────────────────────────────

data class GitHubRelease(
    val tagName: String = "",
    val body: String = "",
    val apkUrl: String = ""
)
