package com.miabisuteri.admin.ui.products

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.miabisuteri.admin.domain.model.COLECCIONES_PRODUCTO
import com.miabisuteri.admin.domain.model.TIPOS_PRODUCTO
import com.miabisuteri.admin.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditScreen(
    productId: Int?,
    onNavigateBack: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val state by viewModel.editState.collectAsStateWithLifecycle()

    LaunchedEffect(productId) { viewModel.loadProduct(productId) }
    LaunchedEffect(state.saved) { if (state.saved) onNavigateBack() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadImage(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (state.isNew) "Nuevo producto" else "Editar producto",
                        color = TextoPrimario
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = VerdeClaro)
                    }
                },
                actions = {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp).padding(end = 8.dp),
                            color = VerdeClaro,
                            strokeWidth = 2.dp
                        )
                    } else {
                        IconButton(onClick = viewModel::saveProduct) {
                            Icon(Icons.Default.Save, "Guardar", tint = VerdeClaro)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AdminSurface)
            )
        },
        containerColor = AdminBackground
    ) { padding ->
        val p = state.producto
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Images section
            Surface(shape = RoundedCornerShape(12.dp), color = AdminCard) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Imágenes", style = MaterialTheme.typography.titleMedium, color = TextoPrimario)
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val context = LocalContext.current
                        p.imgs.forEach { url ->
                            Box {
                                if (url.startsWith("http")) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(url)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(90.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Surface(
                                        modifier = Modifier
                                            .size(90.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        color = AdminSurface2
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Icon(Icons.Default.Image, null, tint = AdminBorder, modifier = Modifier.size(24.dp))
                                                Text("Sin foto", style = MaterialTheme.typography.labelSmall, color = TextoSecundario)
                                            }
                                        }
                                    }
                                }
                                IconButton(
                                    onClick = { viewModel.removeImage(url) },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(24.dp)
                                        .background(
                                            MaterialTheme.colorScheme.error,
                                            RoundedCornerShape(50)
                                        )
                                ) {
                                    Icon(
                                        Icons.Default.Close, null,
                                        tint = MaterialTheme.colorScheme.onError,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                        // Add image button
                        Surface(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier.size(90.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = AdminSurface
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (state.isUploadingImage) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = VerdeClaro,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(Icons.Default.AddPhotoAlternate, "Agregar foto", tint = VerdeClaro)
                                }
                            }
                        }
                    }
                }
            }

            // Basic fields
            Surface(shape = RoundedCornerShape(12.dp), color = AdminCard) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Información", style = MaterialTheme.typography.titleMedium, color = TextoPrimario)

                    MiaTextField(
                        value = p.nombre,
                        onValueChange = { viewModel.onFieldChange { c -> c.copy(nombre = it) } },
                        label = "Nombre"
                    )

                    MiaTextField(
                        value = if (p.precio == 0L) "" else p.precio.toString(),
                        onValueChange = { v ->
                            viewModel.onFieldChange { c -> c.copy(precio = v.toLongOrNull() ?: 0L) }
                        },
                        label = "Precio (ARS)",
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    )

                    MiaTextField(
                        value = p.desc,
                        onValueChange = { viewModel.onFieldChange { c -> c.copy(desc = it) } },
                        label = "Descripción",
                        singleLine = false,
                        minLines = 3
                    )
                }
            }

            // Tipo
            Surface(shape = RoundedCornerShape(12.dp), color = AdminCard) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Tipo", style = MaterialTheme.typography.titleMedium, color = TextoPrimario)
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TIPOS_PRODUCTO.forEach { tipo ->
                            FilterChip(
                                selected = p.tipo == tipo,
                                onClick = { viewModel.onFieldChange { c -> c.copy(tipo = tipo) } },
                                label = { Text(tipo.replaceFirstChar { it.uppercase() }) }
                            )
                        }
                    }
                }
            }

            // Colección
            Surface(shape = RoundedCornerShape(12.dp), color = AdminCard) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Colección", style = MaterialTheme.typography.titleMedium, color = TextoPrimario)
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        COLECCIONES_PRODUCTO.forEach { col ->
                            FilterChip(
                                selected = p.col == col,
                                onClick = { viewModel.onFieldChange { c -> c.copy(col = col) } },
                                label = { Text(col.replaceFirstChar { it.uppercase() }) }
                            )
                        }
                    }
                }
            }

            // Error message
            state.error?.let { error ->
                Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.errorContainer) {
                    Text(
                        error,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Save button
            Button(
                onClick = viewModel::saveProduct,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = VerdeClaro)
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = TextoPrimario, strokeWidth = 2.dp)
                } else {
                    Text("Guardar producto", color = TextoPrimario)
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MiaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: androidx.compose.ui.text.input.KeyboardType = androidx.compose.ui.text.input.KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VerdeClaro,
            focusedLabelColor = VerdeClaro,
            cursorColor = VerdeClaro
        )
    )
}
