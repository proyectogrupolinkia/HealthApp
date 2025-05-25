
package com.tusalud.healthapp.presentation.menu.progress.peso

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualizarPesoScreen(
    navController: NavHostController,
    viewModel: ActualizarPesoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Estado del campo de entrada y su validez


    var nuevoPeso by remember { mutableStateOf("") }
    var pesoValido by remember { mutableStateOf(false) }

    // Estado reactivo del snackbar (desde el ViewModel)

    val snackbarActivo by viewModel.snackbarActivo.collectAsState()
    val snackbarMensaje by viewModel.snackbarMensaje.collectAsState()

    // Launcher para pedir permiso de notificación

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* resultado del permiso */ }
    )
    // Solicita permiso si es necesario

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    // Muestra el snackbar cuando snackbarActivo se vuelve true

    LaunchedEffect(snackbarActivo) {
        if (snackbarActivo) {
            scope.launch {
                snackbarHostState.showSnackbar(snackbarMensaje)
                viewModel.resetSnackbar()
                nuevoPeso = ""
                pesoValido = false
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Actualizar peso") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            // Campo para ingresar el nuevo peso

            OutlinedTextField(
                value = nuevoPeso,
                onValueChange = {
                    if (it.isEmpty()) {
                        nuevoPeso = ""
                        pesoValido = false
                    } else if (viewModel.validarPeso(it)) {
                        nuevoPeso = it
                        pesoValido = true
                    } else if (it.matches(Regex("^\\d{1,3}(\\.\\d{0,2})?$"))) {
                        nuevoPeso = it
                        pesoValido = viewModel.validarPeso(it)
                    }
                },
                label = { Text("Nuevo peso (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = nuevoPeso.isNotEmpty() && !pesoValido,
                modifier = Modifier.fillMaxWidth()
            )

            // Mensaje de error si el peso es inválido

            if (nuevoPeso.isNotEmpty() && !pesoValido) {
                Text(
                    text = "Ingrese un peso válido entre 20 y 500 kg (máx 2 decimales)",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            // Botón para guardar el peso

            Button(
                onClick = {
                    viewModel.actualizarPeso(context, nuevoPeso.toFloatOrNull())
                },
                enabled = pesoValido,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}
