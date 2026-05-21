package com.miabisuteri.admin.ui.auth

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.miabisuteri.admin.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) onLoginSuccess()
    }

    // Launch biometric on enter if available
    LaunchedEffect(Unit) {
        if (viewModel.isBiometricAvailable(context)) {
            launchBiometric(context, onSuccess = { viewModel.onBiometricSuccess() })
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AdminBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Logo / title
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Mía Bisutería",
                    style = MaterialTheme.typography.displayLarge,
                    color = TextoPrimario
                )
                Text(
                    text = "Panel de administración",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }

            // Auth card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = AdminCard,
                tonalElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (!uiState.showPin) {
                        PasswordForm(
                            password = uiState.password,
                            onPasswordChange = viewModel::onPasswordChange,
                            onSubmit = viewModel::onPasswordSubmit,
                            error = uiState.error,
                            isFirstSetup = uiState.isFirstSetup
                        )
                    } else {
                        PinForm(
                            pin = uiState.pin,
                            onPinChange = viewModel::onPinChange,
                            onSubmit = viewModel::onPinSubmit,
                            error = uiState.error,
                            onSwitchToPassword = viewModel::onSwitchToPassword
                        )
                    }
                }
            }

            // Biometric button
            if (!uiState.showPin && viewModel.isBiometricAvailable(context) && viewModel.isBiometricEnabled()) {
                IconButton(
                    onClick = {
                        launchBiometric(context, onSuccess = { viewModel.onBiometricSuccess() })
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Usar biométrico",
                        modifier = Modifier.size(48.dp),
                        tint = VerdeClaro
                    )
                }
            }
        }
    }
}

@Composable
private fun PasswordForm(
    password: String,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    error: String?,
    isFirstSetup: Boolean
) {
    var visible by remember { mutableStateOf(false) }

    Text(
        text = if (isFirstSetup) "Crear contraseña" else "Ingresar contraseña",
        style = MaterialTheme.typography.titleMedium,
        color = TextoPrimario
    )

    if (isFirstSetup) {
        Text(
            text = "Primera vez: creá tu contraseña de acceso.",
            style = MaterialTheme.typography.bodySmall,
            color = TextoSecundario
        )
    }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Contraseña") },
        leadingIcon = { Icon(Icons.Default.Lock, null) },
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onSubmit() }),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VerdeClaro,
            focusedLabelColor = VerdeClaro,
            cursorColor = VerdeClaro
        )
    )

    AnimatedVisibility(visible = error != null) {
        Text(
            text = error ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }

    Button(
        onClick = onSubmit,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = VerdeClaro)
    ) {
        Text(if (isFirstSetup) "Crear y entrar" else "Entrar", color = AdminBackground)
    }
}

@Composable
private fun PinForm(
    pin: String,
    onPinChange: (String) -> Unit,
    onSubmit: () -> Unit,
    error: String?,
    onSwitchToPassword: () -> Unit
) {
    Text(
        text = "Ingresar PIN",
        style = MaterialTheme.typography.titleMedium,
        color = TextoPrimario
    )

    // PIN dot display
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(4) { i ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(16.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (i < pin.length) VerdeClaro else AdminBorder)
            )
        }
    }

    // Number pad
    val digits = listOf("1","2","3","4","5","6","7","8","9","","0","⌫")
    digits.chunked(3).forEach { row ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            row.forEach { d ->
                if (d.isEmpty()) {
                    Spacer(Modifier.size(72.dp))
                } else {
                    Surface(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(50))
                            .clickable {
                                when {
                                    d == "⌫" -> if (pin.isNotEmpty()) onPinChange(pin.dropLast(1))
                                    pin.length < 4 -> {
                                        val newPin = pin + d
                                        onPinChange(newPin)
                                        if (newPin.length == 4) onSubmit()
                                    }
                                }
                            },
                        color = AdminCard,
                        shape = RoundedCornerShape(50)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(d, style = MaterialTheme.typography.titleLarge, color = TextoPrimario)
                        }
                    }
                }
            }
        }
    }

    AnimatedVisibility(visible = error != null) {
        Text(
            text = error ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }

    TextButton(
        onClick = onSwitchToPassword,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Usar contraseña", color = VerdeClaro)
    }
}

private fun launchBiometric(context: android.content.Context, onSuccess: () -> Unit) {
    val executor = ContextCompat.getMainExecutor(context)
    val activity = context as? FragmentActivity ?: return

    val biometricPrompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }
        }
    )

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Verificar identidad")
        .setSubtitle("Para acceder al panel de administración")
        .setNegativeButtonText("Cancelar")
        .build()

    biometricPrompt.authenticate(promptInfo)
}
