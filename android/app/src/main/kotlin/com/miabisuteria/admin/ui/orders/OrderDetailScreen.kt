package com.miabisuteri.admin.ui.orders

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miabisuteri.admin.domain.model.EstadoPedido
import com.miabisuteri.admin.domain.model.Pedido
import com.miabisuteri.admin.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: String,
    onNavigateBack: () -> Unit,
    onOpenCalculator: () -> Unit,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val state by viewModel.detailState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showEstadoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(orderId) { viewModel.loadDetail(orderId) }

    if (showEstadoDialog) {
        EstadoPickerDialog(
            current = state.pedido?.estado ?: EstadoPedido.PENDIENTE_PAGO,
            onSelect = { nuevo ->
                viewModel.updateEstado(orderId, nuevo)
                showEstadoDialog = false
            },
            onDismiss = { showEstadoDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del pedido") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = VerdeClaro)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AdminSurface)
            )
        },
        containerColor = AdminBackground
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VerdeClaro)
            }
            return@Scaffold
        }

        val pedido = state.pedido ?: run {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Pedido no encontrado", color = TextoSecundario)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Estado card
            Surface(shape = RoundedCornerShape(12.dp), color = AdminCard) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Estado actual", style = MaterialTheme.typography.labelSmall, color = TextoSecundario)
                        Text(
                            pedido.estado.display,
                            style = MaterialTheme.typography.titleMedium,
                            color = pedido.estado.toColor()
                        )
                    }
                    Button(
                        onClick = { showEstadoDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeClaro),
                        enabled = !state.isSaving
                    ) {
                        Text("Cambiar", color = AdminBackground)
                    }
                }
            }

            // Cliente
            InfoCard(title = "Cliente") {
                InfoRow("Nombre", pedido.cliente.nombre.ifBlank { "—" })
                InfoRow("Email", pedido.cliente.email.ifBlank { "—" })
                if (pedido.cliente.telefono.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoRow("WhatsApp", pedido.cliente.telefono)
                        IconButton(onClick = {
                            val phone = pedido.cliente.telefono.filter { it.isDigit() }
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://wa.me/$phone")
                            )
                            context.startActivity(intent)
                        }) {
                            Icon(Icons.Default.Chat, "WhatsApp", tint = VerdeClaro)
                        }
                    }
                }
            }

            // Items o descripción personalizada
            if (pedido.tipo == "personalizado") {
                pedido.personalizado?.let { p ->
                    InfoCard(title = "Pedido personalizado") {
                        InfoRow("Tipo", p.tipo.ifBlank { "—" })
                        InfoRow("Colores", p.colores.ifBlank { "—" })
                        if (p.descripcion.isNotBlank()) {
                            Column {
                                Text("Descripción", style = MaterialTheme.typography.labelSmall, color = TextoSecundario)
                                Text(p.descripcion, style = MaterialTheme.typography.bodyMedium, color = TextoPrimario)
                            }
                        }
                        if (p.referencia.isNotBlank()) {
                            InfoRow("Referencia", p.referencia)
                        }
                    }
                }
            } else if (pedido.items.isNotEmpty()) {
                InfoCard(title = "Ítems del pedido") {
                    pedido.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "${item.cantidad}× ${item.nombre}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextoPrimario,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "$${(item.cantidad * item.precioUnitario).format()}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Oro
                            )
                        }
                    }
                }
            }

            // Costos calculados
            pedido.costos?.let { costos ->
                InfoCard(title = "Costos calculados") {
                    InfoRow("Materiales", "$${costos.materiales.toLong().format()}")
                    InfoRow("Tiempo", "$${costos.tiempoHs}hs")
                    InfoRow("Margen", "${costos.margenPct}%")
                    InfoRow("Envío", "$${costos.envio.toLong().format()}")
                    HorizontalDivider(color = AdminBorder, modifier = Modifier.padding(vertical = 4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", style = MaterialTheme.typography.titleMedium, color = TextoPrimario)
                        Text("$${costos.total.format()}", style = MaterialTheme.typography.titleMedium, color = Oro)
                    }
                }
            }

            // Pago
            pedido.pago?.let { pago ->
                if (pago.estado.isNotBlank()) {
                    InfoCard(title = "Pago") {
                        InfoRow("Estado", pago.estado)
                        if (pago.linkPago.isNotBlank()) {
                            TextButton(onClick = {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(pago.linkPago)))
                            }) {
                                Text("Ver link de pago", color = VerdeClaro)
                            }
                        }
                    }
                }
            }

            // Fecha
            pedido.creadoEn?.toDate()?.let { date ->
                Text(
                    "Creado el ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es")).format(date)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextoTerciario
                )
            }

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenCalculator,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeClaro),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(VerdeClaro)
                    )
                ) {
                    Icon(Icons.Default.Calculate, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Calculadora")
                }

                if (pedido.costos != null && pedido.cliente.telefono.isNotBlank()) {
                    Button(
                        onClick = {
                            val msg = buildWhatsAppMsg(pedido)
                            val phone = pedido.cliente.telefono.filter { it.isDigit() }
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://wa.me/$phone?text=${Uri.encode(msg)}")
                            )
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeClaro)
                    ) {
                        Icon(Icons.Default.Chat, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Enviar precio", color = AdminBackground)
                    }
                }
            }

            // Generar link de pago
            val linkActual = state.linkPago ?: pedido.pago?.linkPago?.takeIf { it.isNotBlank() }
            if (pedido.costos != null) {
                if (linkActual != null) {
                    // Link ya generado — mostrar opciones
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Link de pago", linkActual))
                                Toast.makeText(context, "Link copiado", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Oro),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(Oro)
                            )
                        ) {
                            Icon(Icons.Default.ContentCopy, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Copiar link")
                        }
                        if (pedido.cliente.telefono.isNotBlank()) {
                            Button(
                                onClick = {
                                    val phone = pedido.cliente.telefono.filter { it.isDigit() }
                                    val msg = "Hola ${pedido.cliente.nombre}! Te mando el link para abonar tu pedido de Mía Bisutería: $linkActual"
                                    context.startActivity(Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://wa.me/$phone?text=${Uri.encode(msg)}")
                                    ))
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Oro)
                            ) {
                                Icon(Icons.Default.Chat, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Enviar link", color = AdminBackground)
                            }
                        }
                    }
                } else {
                    // Sin link — botón para generarlo
                    Button(
                        onClick = { viewModel.generarLinkPago(orderId) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isGeneratingLink,
                        colors = ButtonDefaults.buttonColors(containerColor = Oro)
                    ) {
                        if (state.isGeneratingLink) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = AdminBackground, strokeWidth = 2.dp)
                            Spacer(Modifier.width(8.dp))
                            Text("Generando link...", color = AdminBackground)
                        } else {
                            Icon(Icons.Default.Link, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Generar link de pago", color = AdminBackground)
                        }
                    }
                }
            }

            // Error
            state.error?.let { err ->
                Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.errorContainer) {
                    Text(err, modifier = Modifier.padding(12.dp), color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun InfoCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(shape = RoundedCornerShape(12.dp), color = AdminCard, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = TextoSecundario)
            HorizontalDivider(color = AdminBorder)
            content()
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = TextoPrimario)
    }
}

@Composable
private fun EstadoPickerDialog(
    current: EstadoPedido,
    onSelect: (EstadoPedido) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cambiar estado") },
        text = {
            Column {
                EstadoPedido.entries.forEach { estado ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = estado == current,
                            onClick = { onSelect(estado) },
                            colors = RadioButtonDefaults.colors(selectedColor = VerdeClaro)
                        )
                        Text(estado.display, color = TextoPrimario)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

private fun EstadoPedido.toColor(): Color = when (this) {
    EstadoPedido.PENDIENTE_PAGO, EstadoPedido.PAGO_CONFIRMADO -> EstadoNuevo
    EstadoPedido.EN_FABRICACION, EstadoPedido.EN_PROCESO -> EstadoEnProceso
    EstadoPedido.LISTO, EstadoPedido.ENTREGADO -> EstadoListo
    EstadoPedido.CANCELADO -> EstadoCancelado
}

private fun buildWhatsAppMsg(pedido: Pedido): String {
    val costos = pedido.costos ?: return ""
    return buildString {
        append("Hola ${pedido.cliente.nombre}! Te paso el precio de tu pedido:\n\n")
        if (pedido.tipo == "personalizado") {
            append("Pedido personalizado\n")
            pedido.personalizado?.let { append("${it.tipo} - ${it.colores}\n") }
        } else {
            pedido.items.forEach { append("${it.cantidad}× ${it.nombre}\n") }
        }
        append("\nMateriales: $${costos.materiales.toLong().format()}")
        append("\nMargen: ${costos.margenPct}%")
        if (costos.envio > 0) append("\nEnvío: $${costos.envio.toLong().format()}")
        append("\n\n*Total: $${costos.total.format()}*")
    }
}

private fun Long.format(): String = "%,d".format(this).replace(',', '.')
