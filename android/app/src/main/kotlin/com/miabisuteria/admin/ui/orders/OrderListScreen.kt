package com.miabisuteri.admin.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
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
fun OrderListScreen(
    onNavigateBack: () -> Unit,
    onOpenOrder: (String) -> Unit,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val state by viewModel.listState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Nuevos", "En proceso", "Listos", "Historial")

    val tabPedidos = when (selectedTab) {
        0 -> viewModel.pedidosNuevos(state.pedidos)
        1 -> viewModel.pedidosEnProceso(state.pedidos)
        2 -> viewModel.pedidosListos(state.pedidos)
        else -> viewModel.pedidosHistorial(state.pedidos)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Pedidos") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.ArrowBack, null, tint = VerdeClaro)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = AdminSurface)
                )
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = AdminSurface2,
                    contentColor = VerdeClaro,
                    indicator = { tabPositions ->
                        Box(
                            Modifier
                                .tabIndicatorOffset(tabPositions[selectedTab])
                                .height(2.dp)
                                .fillMaxWidth()
                                .background(VerdeClaro)
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                val count = when (index) {
                                    0 -> viewModel.pedidosNuevos(state.pedidos).size
                                    1 -> viewModel.pedidosEnProceso(state.pedidos).size
                                    2 -> viewModel.pedidosListos(state.pedidos).size
                                    else -> viewModel.pedidosHistorial(state.pedidos).size
                                }
                                val label = if (count > 0) "$title ($count)" else title
                                Text(label, style = MaterialTheme.typography.labelSmall)
                            }
                        )
                    }
                }
            }
        },
        containerColor = AdminBackground
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VerdeClaro)
            }
        } else if (tabPedidos.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Sin pedidos en esta sección", color = TextoSecundario)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tabPedidos, key = { it.id }) { pedido ->
                    OrderCard(pedido = pedido, onClick = { onOpenOrder(pedido.id) })
                }
            }
        }
    }
}

@Composable
private fun OrderCard(pedido: Pedido, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = AdminCard
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status dot
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .padding(top = 4.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = pedido.estado.toColor(),
                    modifier = Modifier.fillMaxSize()
                ) {}
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    pedido.cliente.nombre.ifBlank { "Sin nombre" },
                    style = MaterialTheme.typography.titleMedium,
                    color = TextoPrimario,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    pedido.estado.display,
                    style = MaterialTheme.typography.bodySmall,
                    color = pedido.estado.toColor()
                )
                if (pedido.tipo == "personalizado") {
                    Text(
                        "Pedido personalizado",
                        style = MaterialTheme.typography.bodySmall,
                        color = Oro
                    )
                } else {
                    val itemCount = pedido.items.sumOf { it.cantidad }
                    Text(
                        "$itemCount ${if (itemCount == 1) "ítem" else "ítems"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
                pedido.creadoEn?.toDate()?.let { date ->
                    Text(
                        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es")).format(date),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextoTerciario
                    )
                }
            }

            pedido.costos?.let {
                Text("$${it.total.format()}", style = MaterialTheme.typography.bodyMedium, color = Oro)
            }

            Icon(Icons.Default.ChevronRight, null, tint = AdminBorder)
        }
    }
}

private fun EstadoPedido.toColor(): Color = when (this) {
    EstadoPedido.PENDIENTE_PAGO -> EstadoNuevo
    EstadoPedido.PAGO_CONFIRMADO -> EstadoNuevo
    EstadoPedido.EN_PROCESO -> EstadoEnProceso
    EstadoPedido.LISTO -> EstadoListo
    EstadoPedido.CANCELADO -> EstadoCancelado
}

private fun Long.format(): String = "%,d".format(this).replace(',', '.')
