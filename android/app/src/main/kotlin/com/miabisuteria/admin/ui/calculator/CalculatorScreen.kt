package com.miabisuteri.admin.ui.calculator

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miabisuteri.admin.domain.model.Material
import com.miabisuteri.admin.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    orderId: String?,
    onNavigateBack: () -> Unit,
    viewModel: CalculatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.recalculate() }
    LaunchedEffect(uiState.saved) { if (uiState.saved) onNavigateBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculadora") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Materiales
            CalcSection(title = "Materiales", icon = Icons.Default.Diamond) {
                viewModel.materiales.forEachIndexed { index, material ->
                    MaterialRow(
                        material = material,
                        index = index,
                        canRemove = viewModel.materiales.size > 1,
                        onNameChange = { viewModel.updateMaterialName(index, it) },
                        onCostChange = { viewModel.updateMaterialCost(index, it) },
                        onRemove = { viewModel.removeMaterial(index) }
                    )
                }
                OutlinedButton(
                    onClick = viewModel::addMaterial,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeClaro),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(VerdeClaro)
                    )
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Agregar material")
                }
            }

            // Tiempo de trabajo
            CalcSection(title = "Tiempo de trabajo", icon = Icons.Default.AccessTime) {
                NumberField(
                    label = "Horas estimadas",
                    value = viewModel.horasEstimadas.value,
                    step = 0.5,
                    onValueChange = { viewModel.updateHoras(it) }
                )
                NumberField(
                    label = "Valor por hora (ARS)",
                    value = viewModel.valorHora.value,
                    step = 100.0,
                    onValueChange = { viewModel.updateValorHora(it) }
                )
            }

            // Envío
            CalcSection(title = "Envío", icon = Icons.Default.LocalShipping) {
                NumberField(
                    label = "Costo de envío (ARS) — 0 si retira",
                    value = viewModel.costoEnvio.value,
                    step = 100.0,
                    onValueChange = { viewModel.updateEnvio(it) }
                )
            }

            // Margen
            CalcSection(title = "Margen de ganancia", icon = Icons.Default.Percent) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Margen", style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                    Text(
                        "${viewModel.margenPct.value}%",
                        style = MaterialTheme.typography.titleMedium,
                        color = VerdeClaro
                    )
                }
                Slider(
                    value = viewModel.margenPct.value.toFloat(),
                    onValueChange = { viewModel.updateMargen(it.toInt()) },
                    valueRange = 10f..100f,
                    steps = 89,
                    colors = SliderDefaults.colors(
                        thumbColor = VerdeClaro,
                        activeTrackColor = VerdeClaro,
                        inactiveTrackColor = AdminBorder
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("10%", style = MaterialTheme.typography.labelSmall, color = TextoTerciario)
                    Text("100%", style = MaterialTheme.typography.labelSmall, color = TextoTerciario)
                }
            }

            // Resultado
            Surface(shape = RoundedCornerShape(12.dp), color = AdminCard) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Resultado", style = MaterialTheme.typography.titleSmall, color = TextoSecundario)
                    HorizontalDivider(color = AdminBorder)
                    ResultRow("Materiales", uiState.resultado.totalMateriales)
                    ResultRow("Tiempo de trabajo", uiState.resultado.costoTiempo)
                    ResultRow("Margen de ganancia", uiState.resultado.margenMonto)
                    ResultRow("Envío", uiState.resultado.costoEnvio)
                    HorizontalDivider(color = AdminBorder)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("TOTAL", style = MaterialTheme.typography.titleLarge, color = TextoPrimario)
                        Text(
                            formatArs(uiState.resultado.total),
                            style = MaterialTheme.typography.titleLarge,
                            color = Oro
                        )
                    }
                }
            }

            uiState.error?.let { error ->
                Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.errorContainer) {
                    Text(error, modifier = Modifier.padding(12.dp), color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }

            // Actions
            if (orderId != null) {
                Button(
                    onClick = { viewModel.saveToOrder(orderId) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isSaving,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeClaro)
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = AdminBackground, strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.Save, null, modifier = Modifier.size(18.dp), tint = AdminBackground)
                        Spacer(Modifier.width(8.dp))
                        Text("Guardar en pedido", color = AdminBackground)
                    }
                }
            }

            // Share via WhatsApp
            OutlinedButton(
                onClick = {
                    val total = formatArs(uiState.resultado.total)
                    val msg = "El precio total de tu pedido es $total"
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://wa.me/?text=${Uri.encode(msg)}")
                    )
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeClaro),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(VerdeClaro)
                )
            ) {
                Icon(Icons.Default.Share, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Compartir por WhatsApp")
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CalcSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(shape = RoundedCornerShape(12.dp), color = AdminCard) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = VerdeClaro, modifier = Modifier.size(20.dp))
                Text(title, style = MaterialTheme.typography.titleMedium, color = TextoPrimario)
            }
            content()
        }
    }
}

@Composable
private fun MaterialRow(
    material: Material,
    index: Int,
    canRemove: Boolean,
    onNameChange: (String) -> Unit,
    onCostChange: (Double) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = material.nombre,
            onValueChange = onNameChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Material") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdeClaro,
                cursorColor = VerdeClaro
            )
        )
        OutlinedTextField(
            value = if (material.costo == 0.0) "" else material.costo.toDisplayString(),
            onValueChange = { raw ->
                val parsed = raw.replace(",", ".").toDoubleOrNull() ?: 0.0
                onCostChange(parsed)
            },
            modifier = Modifier.width(100.dp),
            placeholder = { Text("$") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdeClaro,
                cursorColor = VerdeClaro
            )
        )
        if (canRemove) {
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Close, "Quitar", tint = MaterialTheme.colorScheme.error)
            }
        } else {
            Spacer(Modifier.size(40.dp))
        }
    }
}

@Composable
private fun NumberField(
    label: String,
    value: Double,
    step: Double,
    onValueChange: (Double) -> Unit
) {
    var text by remember(value) {
        mutableStateOf(if (value == 0.0) "" else value.toDisplayString())
    }
    OutlinedTextField(
        value = text,
        onValueChange = { raw ->
            text = raw
            val parsed = raw.replace(",", ".").toDoubleOrNull() ?: 0.0
            onValueChange(parsed)
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VerdeClaro,
            focusedLabelColor = VerdeClaro,
            cursorColor = VerdeClaro
        )
    )
}

@Composable
private fun ResultRow(label: String, value: Double) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
        Text(formatArs(value), style = MaterialTheme.typography.bodyMedium, color = TextoPrimario)
    }
}

private fun formatArs(value: Double): String {
    if (value == 0.0) return "$0"
    return "$" + NumberFormat.getNumberInstance(Locale("es", "AR")).format(value.toLong())
}

private fun Double.toDisplayString(): String {
    return if (this == this.toLong().toDouble()) this.toLong().toString()
    else this.toString()
}
