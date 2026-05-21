package com.miabisuteri.admin.ui.config

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.miabisuteri.admin.domain.model.Coleccion
import com.miabisuteri.admin.domain.model.Producto
import com.miabisuteri.admin.domain.model.Testimonio
import com.miabisuteri.admin.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    onNavigateBack: () -> Unit,
    viewModel: ConfigViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var expandedSection by remember { mutableStateOf<String?>("destacado") }

    LaunchedEffect(state.saved) {
        if (state.saved) viewModel.dismissSaved()
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { viewModel.uploadGalleryImage(it) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración", color = TextoPrimario) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = VerdeClaro)
                    }
                },
                actions = {
                    if (state.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = VerdeClaro, strokeWidth = 2.dp)
                    } else {
                        IconButton(onClick = viewModel::saveConfig) {
                            Icon(Icons.Default.Save, "Guardar", tint = VerdeClaro)
                        }
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Producto destacado
            ExpandableSection(
                title = "Producto destacado (Hero)",
                icon = Icons.Default.Star,
                expanded = expandedSection == "destacado",
                onToggle = { expandedSection = if (expandedSection == "destacado") null else "destacado" }
            ) {
                if (state.config.destacado.nombre.isNotBlank()) {
                    Text("Actual: ${state.config.destacado.nombre}", color = TextoPrimario)
                    Text("$${state.config.destacado.precio}", color = Oro)
                }
                Text("Seleccionar producto:", color = VerdeClaro, style = MaterialTheme.typography.labelMedium)
                state.productos.forEach { producto ->
                    ProductPickerRow(
                        producto = producto,
                        selected = state.config.destacado.nombre == producto.nombre,
                        onSelect = { viewModel.setDestacado(producto) }
                    )
                }
            }

            // Favoritos
            ExpandableSection(
                title = "Destacados en inicio (4 máx)",
                icon = Icons.Default.Favorite,
                expanded = expandedSection == "favoritos",
                onToggle = { expandedSection = if (expandedSection == "favoritos") null else "favoritos" }
            ) {
                Text("${state.config.favoritos.size}/4 seleccionados", color = VerdeClaro)
                state.productos.forEach { producto ->
                    ProductPickerRow(
                        producto = producto,
                        selected = producto.id in state.config.favoritos,
                        onSelect = { viewModel.toggleFavorito(producto.id) },
                        checkbox = true
                    )
                }
            }

            // Colecciones
            ExpandableSection(
                title = "Colecciones",
                icon = Icons.Default.Collections,
                expanded = expandedSection == "colecciones",
                onToggle = { expandedSection = if (expandedSection == "colecciones") null else "colecciones" }
            ) {
                state.config.colecciones.forEachIndexed { index, col ->
                    ColeccionEditor(
                        col = col,
                        onChange = { viewModel.updateColeccion(index, it) }
                    )
                    if (index < state.config.colecciones.lastIndex) {
                        HorizontalDivider(color = AdminBorder)
                    }
                }
            }

            // Testimonios
            ExpandableSection(
                title = "Testimonios",
                icon = Icons.Default.RateReview,
                expanded = expandedSection == "testimonios",
                onToggle = { expandedSection = if (expandedSection == "testimonios") null else "testimonios" }
            ) {
                state.config.testimonios.forEachIndexed { index, t ->
                    TestimonioEditor(
                        testimonio = t,
                        onChange = { viewModel.updateTestimonio(index, it) },
                        onRemove = { viewModel.removeTestimonio(index) }
                    )
                }
                OutlinedButton(
                    onClick = viewModel::addTestimonio,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeClaro),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(VerdeClaro)
                    )
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Agregar testimonio")
                }
            }

            // Galería personalizada
            ExpandableSection(
                title = "Galería personalizada",
                icon = Icons.Default.PhotoLibrary,
                expanded = expandedSection == "galeria",
                onToggle = { expandedSection = if (expandedSection == "galeria") null else "galeria" }
            ) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.config.galeria.forEach { url ->
                        Box {
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { viewModel.removeGalleryImage(url) },
                                modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                    Surface(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.size(90.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = AdminSurface
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (state.isUploadingImage) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = VerdeClaro, strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.AddPhotoAlternate, null, tint = VerdeClaro)
                            }
                        }
                    }
                }
            }

            state.error?.let { error ->
                Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.errorContainer) {
                    Text(error, modifier = Modifier.padding(12.dp), color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }

            Button(
                onClick = viewModel::saveConfig,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = VerdeClaro)
            ) {
                Text("Guardar configuración", color = TextoPrimario)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ExpandableSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(shape = RoundedCornerShape(12.dp), color = AdminCard, modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, null, tint = VerdeClaro, modifier = Modifier.size(20.dp))
                    Text(title, style = MaterialTheme.typography.titleMedium, color = TextoPrimario)
                }
                IconButton(onClick = onToggle) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        null, tint = VerdeClaro
                    )
                }
            }
            if (expanded) {
                HorizontalDivider(color = AdminBorder)
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun ProductPickerRow(
    producto: Producto,
    selected: Boolean,
    onSelect: () -> Unit,
    checkbox: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (checkbox) {
            Checkbox(
                checked = selected,
                onCheckedChange = { onSelect() },
                colors = CheckboxDefaults.colors(checkedColor = VerdeClaro)
            )
        } else {
            RadioButton(
                selected = selected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(selectedColor = VerdeClaro)
            )
        }
        if (producto.imgs.isNotEmpty()) {
            AsyncImage(
                model = producto.imgs.first(),
                contentDescription = null,
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(8.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                producto.nombre,
                color = TextoPrimario,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text("$${producto.precio}", color = Oro, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun ColeccionEditor(col: Coleccion, onChange: (Coleccion) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = col.emoji,
                onValueChange = { onChange(col.copy(emoji = it)) },
                modifier = Modifier.width(64.dp),
                label = { Text("Emoji") },
                singleLine = true
            )
            OutlinedTextField(
                value = col.nombre,
                onValueChange = { onChange(col.copy(nombre = it)) },
                modifier = Modifier.weight(1f),
                label = { Text("Nombre") },
                singleLine = true
            )
        }
    }
}

@Composable
private fun TestimonioEditor(testimonio: Testimonio, onChange: (Testimonio) -> Unit, onRemove: () -> Unit) {
    Surface(shape = RoundedCornerShape(8.dp), color = AdminSurface, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = testimonio.nombre,
                        onValueChange = { onChange(testimonio.copy(nombre = it)) },
                        modifier = Modifier.weight(1f),
                        label = { Text("Nombre") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = testimonio.ciudad,
                        onValueChange = { onChange(testimonio.copy(ciudad = it)) },
                        modifier = Modifier.weight(1f),
                        label = { Text("Ciudad") },
                        singleLine = true
                    )
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Estrellas:", color = VerdeClaro)
                (1..5).forEach { star ->
                    IconButton(onClick = { onChange(testimonio.copy(estrellas = star)) }, modifier = Modifier.size(32.dp)) {
                        Icon(
                            if (star <= testimonio.estrellas) Icons.Default.Star else Icons.Default.StarOutline,
                            null, tint = Oro, modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            OutlinedTextField(
                value = testimonio.texto,
                onValueChange = { onChange(testimonio.copy(texto = it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Texto del testimonio") },
                minLines = 2
            )
        }
    }
}
