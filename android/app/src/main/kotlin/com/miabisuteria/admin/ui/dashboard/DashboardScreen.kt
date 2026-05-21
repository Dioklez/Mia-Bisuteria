package com.miabisuteri.admin.ui.dashboard

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miabisuteri.admin.domain.model.GitHubRelease
import com.miabisuteri.admin.ui.update.UpdateDialog
import com.miabisuteri.admin.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateProducts: () -> Unit,
    onNavigateOrders: () -> Unit,
    onNavigateConfig: () -> Unit,
    onNavigateCalculator: () -> Unit,
    onLogout: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    state.updateAvailable?.let { release ->
        UpdateDialog(
            release = release,
            onDismiss = viewModel::dismissUpdate,
            onUpdate = { apkUrl ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(apkUrl))
                context.startActivity(intent)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mía Bisutería",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, "Salir", tint = TextoSecundario)
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
            // Stats
            Text(
                "Resumen",
                style = MaterialTheme.typography.titleSmall,
                color = TextoSecundario
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Productos",
                    value = state.totalProductos.toString(),
                    icon = Icons.Default.Inventory2,
                    loading = state.isLoading
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Pedidos nuevos",
                    value = state.pedidosNuevos.toString(),
                    icon = Icons.Default.ShoppingBag,
                    loading = state.isLoading,
                    highlight = state.pedidosNuevos > 0
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "En proceso",
                    value = state.pedidosEnProceso.toString(),
                    icon = Icons.Default.PendingActions,
                    loading = state.isLoading
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Ingresos del mes",
                    value = formatArs(state.ingresosMes),
                    icon = Icons.Default.AttachMoney,
                    loading = state.isLoading
                )
            }

            Spacer(Modifier.height(4.dp))

            // Navigation actions
            Text(
                "Secciones",
                style = MaterialTheme.typography.titleSmall,
                color = TextoSecundario
            )

            NavActionCard(
                title = "Pedidos",
                subtitle = "Ver y gestionar pedidos",
                icon = Icons.Default.ShoppingBag,
                badge = if (state.pedidosNuevos > 0) state.pedidosNuevos else null,
                onClick = onNavigateOrders
            )
            NavActionCard(
                title = "Productos",
                subtitle = "CRUD de catálogo",
                icon = Icons.Default.Inventory2,
                onClick = onNavigateProducts
            )
            NavActionCard(
                title = "Calculadora",
                subtitle = "Calcular precio de pedido personalizado",
                icon = Icons.Default.Calculate,
                onClick = onNavigateCalculator
            )
            NavActionCard(
                title = "Configuración",
                subtitle = "Colecciones, testimonios, galería",
                icon = Icons.Default.Settings,
                onClick = onNavigateConfig
            )
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    loading: Boolean,
    highlight: Boolean = false
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (highlight) VerdeClaro.copy(alpha = 0.15f) else AdminCard,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (highlight) VerdeClaro else TextoTerciario,
                modifier = Modifier.size(20.dp)
            )
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = VerdeClaro, strokeWidth = 2.dp)
            } else {
                Text(
                    value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (highlight) VerdeClaro else TextoPrimario,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextoSecundario)
        }
    }
}

@Composable
private fun NavActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badge: Int? = null,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = AdminCard
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Icon(icon, contentDescription = null, tint = VerdeClaro, modifier = Modifier.size(26.dp))
                if (badge != null) {
                    Badge(
                        containerColor = Oro,
                        modifier = Modifier.align(Alignment.TopEnd).offset(x = 4.dp, y = (-4).dp)
                    ) {
                        Text(badge.toString(), color = AdminBackground)
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = TextoPrimario)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
            }
            Icon(Icons.Default.ChevronRight, null, tint = AdminBorder)
        }
    }
}

private fun formatArs(amount: Long): String {
    if (amount == 0L) return "$0"
    return "$" + NumberFormat.getNumberInstance(Locale("es", "AR")).format(amount)
}
