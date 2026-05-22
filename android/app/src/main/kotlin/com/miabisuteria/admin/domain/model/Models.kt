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

val PRODUCTOS_DEFAULT = listOf(
    Producto(0,  "Anillo de Flores Rosa",            2500,  "anillo",    "flores",    "Hecho a mano con mostacillas checas y componentes gold filled.", listOf("images/anillo-flores-rosa.jpeg")),
    Producto(1,  "Anillo de Flores Blanco",          2500,  "anillo",    "flores",    "Hecho a mano con mostacillas checas y componentes gold filled.", listOf("images/anillo-flores-blanco.jpeg")),
    Producto(2,  "Argollas Alas en Flor",            11999, "argolla",   "mariposa",  "Base de aro gold filled con dijes de flores hechos a mano.", listOf("images/argollas-alas-flor-1.jpeg","images/argollas-alas-flor-2.jpeg","images/argollas-alas-flor-3.jpeg")),
    Producto(3,  "Argollas Alma de Girasoles",       11999, "argolla",   "girasol",   "Base de aro gold filled con dijes de flores hechos a mano.", listOf("images/argollas-alma-girasol-1.jpeg","images/argollas-alma-girasol-2.jpeg")),
    Producto(4,  "Argollas Lluvia de Margaritas",    13900, "argolla",   "flores",    "Base de aro gold filled con dijes de flores hechos a mano.", listOf("images/argollas-lluvia-margaritas.jpeg")),
    Producto(5,  "Argollas Flor de Nieve",           9900,  "argolla",   "flores",    "Base de aro gold filled con dijes de flores hechos a mano.", listOf("images/argollas-flor-nieve.jpeg")),
    Producto(6,  "Collar Mariposa Lila",             10500, "collar",    "mariposa",  "Hecho a mano con mostacillas Miyuki, cristal de roca y componentes gold filled. Fino, femenino y muy liviano.", listOf("images/collar-mariposa-lila-1.jpeg","images/collar-mariposa-lila-2.jpeg")),
    Producto(7,  "Gargantilla Lluvia de Margaritas", 13490, "collar",    "flores",    "Diseño delicado con mostacillas y componentes gold filled. 37 cm de largo con 5 cm de extensión.", listOf("images/gargantilla-lluvia-margaritas-1.jpeg","images/gargantilla-lluvia-margaritas-2.jpeg","images/gargantilla-lluvia-margaritas-3.jpeg","images/gargantilla-lluvia-margaritas-4.jpeg")),
    Producto(8,  "Tobillera de Caracoles",           4000,  "tobillera", "caracoles", "El accesorio que vuelve todos los veranos. Caracoles naturales, detalles en Miyuki, gold filled y cierre regulable.", listOf("images/tobillera-caracoles-1.jpeg","images/tobillera-caracoles-2.jpeg","images/tobillera-caracoles-3.jpeg","images/tobillera-caracoles-4.jpeg")),
    Producto(9,  "Pulsera Alma de Girasoles",        6499,  "pulsera",   "girasol",   "Hecha a mano con mostacillas checas, cristales checos y componentes gold filled.", listOf("images/pulsera-alma-girasol-1.jpeg","images/pulsera-alma-girasol-2.jpeg","images/pulsera-alma-girasol-3.jpeg")),
    Producto(10, "Pulsera Flor de Nieve",            8700,  "pulsera",   "flores",    "Diseño delicado con mostacillas y componentes gold filled. 16 cm de largo con 5 cm de extensión.", listOf("images/pulsera-flor-nieve.jpeg")),
    Producto(11, "Pulsera Margaritas de Amor Rosa",  8800,  "pulsera",   "flores",    "Diseño delicado con mostacillas, cristales y componentes gold filled. 16 cm de largo con 5 cm de extensión.", listOf("images/margaritas-amor-rosa-1.jpeg","images/margaritas-amor-rosa-2.jpeg")),
    Producto(12, "Pulsera Patria de Flores",         9900,  "pulsera",   "flores",    "Un accesorio que representa identidad, amor y raíces. Mostacillas, cristales y gold filled. 16 cm con 5 cm de extensión.", listOf("images/pulsera-patria-flores-1.jpeg","images/pulsera-patria-flores-2.jpeg","images/pulsera-patria-flores-3.jpeg")),
    Producto(13, "Pulsera Flor Amapola Rosa",        9500,  "pulsera",   "flores",    "Diseño delicado con mostacillas, cristales y componentes gold filled. 16 cm de largo con 5 cm de extensión.", listOf("images/pulsera-flor-amapola-rosa-1.jpeg","images/pulsera-flor-amapola-rosa-2.jpeg")),
    Producto(14, "Pulsera Tierra Floral",            8700,  "pulsera",   "flores",    "Diseño delicado con mostacillas, cristales y componentes gold filled. 16 cm de largo con 5 cm de extensión.", listOf("images/pulsera-tierra-flor-1.jpeg","images/pulsera-tierra-flor-2.jpeg")),
    Producto(15, "Pulsera Margaritas",               9000,  "pulsera",   "flores",    "Hecha a mano con Miyuki rocalla 8/0 y componentes gold filled. 16 cm de largo con 5 cm de extensión.", listOf("images/pulsera-margaritas-1.jpeg","images/pulsera-margaritas-2.jpeg")),
    Producto(16, "Pulsera Cherry con Margaritas",    8650,  "pulsera",   "cherry",    "Diseño delicado con mostacillas, cristales y componentes gold filled. 16 cm de largo con 5 cm de extensión.", listOf("images/pulsera-cherry-margaritas-1.jpeg","images/pulsera-cherry-margaritas-2.jpeg")),
    Producto(17, "Pulsera Mariposa Mía",             8500,  "pulsera",   "mariposa",  "Hecha a mano con Miyuki, cristal de roca y componentes gold filled. 16 cm de largo con 5 cm de extensión.", listOf("images/pulsera-mariposa-mia.jpeg")),
    Producto(18, "Pulsera Suspiro Lila",             8500,  "pulsera",   "flores",    "Hecha a mano con mostacillas, cristales y componentes gold filled.", listOf("images/pulsera-suspiro-lila.jpeg")),
    Producto(19, "Daisy Bracelet con Cristales",     6900,  "pulsera",   "flores",    "Hecha a mano con Miyuki, cristal de roca y componentes gold filled. 16 cm de largo con 5 cm de extensión.", listOf("images/daisy-bracelet-cristales-1.jpeg","images/daisy-bracelet-cristales-2.jpeg","images/daisy-bracelet-cristales-3.jpeg")),
    Producto(20, "Pulsera Rapunzel",                 6900,  "pulsera",   "flores",    "Hecha a mano con Miyuki, cristal de roca, mostacillas checas y componentes gold filled. 16 cm con 5 cm de extensión.", listOf("images/pulsera-rapunzel.jpeg")),
    Producto(21, "Pulsera Miel",                     6900,  "pulsera",   "flores",    "Hecha a mano con Miyuki, cristal de roca y componentes gold filled. 16 cm de largo con 5 cm de extensión.", listOf("images/pulsera-miel-1.jpeg","images/pulsera-miel-2.jpeg")),
    Producto(22, "Pulsera Petalina",                 6900,  "pulsera",   "flores",    "Inspirada en los pétalos más suaves del jardín. Hecha a mano con mostacillas, cristales y componentes gold filled.", listOf("images/pulsera-petalina.jpeg")),
    Producto(23, "Aros Flor de Cristal",             10700, "argolla",   "flores",    "Base de aro gold filled y flores hechas a mano.", listOf("images/aros-flor-cristal.jpeg")),
    Producto(24, "Pulsera Suspiro de Primavera",     7200,  "pulsera",   "flores",    "Hecha a mano con Miyuki y componentes gold filled. 16 cm de largo con 5 cm de extensión.", listOf("images/pulsera-suspiro-primavera.jpeg")),
    Producto(25, "Aros Flor de Cristal Lila",        10700, "argolla",   "flores",    "Base de aro gold filled y flores hechas a mano.", listOf("images/aros-flor-cristal-lila.jpeg")),
)

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
    EN_FABRICACION("en_fabricacion", "En fabricación"),
    EN_PROCESO("en_proceso", "En proceso"),
    LISTO("listo", "Listo para entregar"),
    ENTREGADO("entregado", "Entregado"),
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
