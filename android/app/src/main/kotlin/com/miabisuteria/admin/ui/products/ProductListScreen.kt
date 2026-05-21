package com.miabisuteri.admin.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.miabisuteri.admin.domain.model.TIPOS_PRODUCTO
import com.miabisuteri.admin.domain.model.Producto
import com.miabisuteri.admin.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onNavigateBack: () -> Unit,
    onEditProduct: (Int) -> Unit,
    onNewProduct: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val state by viewModel.listState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf<Int?>(null) }
    var deletePassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.loadAll() }

    showDeleteDialog?.let { id ->
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = null
                deletePassword = ""
                passwordError = false
            },
            title = { Text("Eliminar producto") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("¿Seguro que querés eliminar este producto?")
                    OutlinedTextField(
                        value = deletePassword,
                        onValueChange = { deletePassword = it; passwordError = false },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = passwordError,
                        supportingText = if (passwordError) {{ Text("Contraseña incorrecta") }} else null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.error,
                            errorBorderColor = MaterialTheme.colorScheme.error
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (viewModel.checkPassword(deletePassword)) {
                        viewModel.deleteProduct(id)
                        showDeleteDialog = null
                        deletePassword = ""
                        passwordError = false
                    } else {
                        passwordError = true
                    }
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = null
                    deletePassword = ""
                    passwordError = false
                }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = VerdeClaro)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AdminSurface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewProduct,
                containerColor = VerdeClaro
            ) {
                Icon(Icons.Default.Add, "Nuevo producto", tint = AdminBackground)
            }
        },
        containerColor = AdminBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::onSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar producto...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdeClaro,
                    cursorColor = VerdeClaro
                )
            )

            // Type filter chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = state.filterTipo == null,
                        onClick = { viewModel.onFilterTipo(null) },
                        label = { Text("Todos") }
                    )
                }
                items(TIPOS_PRODUCTO) { tipo ->
                    FilterChip(
                        selected = state.filterTipo == tipo,
                        onClick = { viewModel.onFilterTipo(tipo) },
                        label = { Text(tipo.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            // Count
            Text(
                "${state.filtered.size} productos",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = TextoSecundario
            )

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = VerdeClaro)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.filtered, key = { it.id }) { producto ->
                        ProductCard(
                            producto = producto,
                            onEdit = { onEditProduct(producto.id) },
                            onDelete = { showDeleteDialog = producto.id }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    producto: Producto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = AdminCard
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            if (producto.imgs.isNotEmpty()) {
                AsyncImage(
                    model = producto.imgs.first(),
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = AdminSurface2
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Image, null, tint = AdminBorder)
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextoPrimario,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${producto.tipo} · ${producto.col}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
                Text(
                    "$${producto.precio.format()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Oro
                )
            }

            Column {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "Editar", tint = VerdeClaro)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

private fun Long.format(): String = "%,d".format(this).replace(',', '.')
