package com.miabisuteri.admin.ui.update

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.miabisuteri.admin.domain.model.GitHubRelease
import com.miabisuteri.admin.ui.theme.*

@Composable
fun UpdateDialog(
    release: GitHubRelease,
    onDismiss: () -> Unit,
    onUpdate: (apkUrl: String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(Icons.Default.SystemUpdate, null, tint = VerdeClaro)
        },
        title = {
            Text("Nueva versión disponible", color = VerdeMenta)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Versión ${release.tagName} lista para instalar.",
                    color = VerdeClaro
                )
                if (release.body.isNotBlank()) {
                    HorizontalDivider(color = AdminBorder)
                    Text(
                        "Novedades:",
                        style = MaterialTheme.typography.labelMedium,
                        color = VerdeClaro
                    )
                    Text(
                        release.body.take(300),
                        style = MaterialTheme.typography.bodySmall,
                        color = VerdeMenta
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onUpdate(release.apkUrl) },
                colors = ButtonDefaults.buttonColors(containerColor = VerdeAcento)
            ) {
                Text("Actualizar ahora", color = VerdeMenta)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Más tarde", color = VerdeClaro)
            }
        },
        containerColor = AdminCard
    )
}
