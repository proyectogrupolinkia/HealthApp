package com.tusalud.healthapp.presentation.menu.progress.peso

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tusalud.healthapp.presentation.main.MainViewModel
import kotlinx.coroutines.launch
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualizarPesoScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val notificacionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* manejar si se niega o acepta, opcional */ }
    )

    // 🔐 Solicitar permiso al iniciar si necesario
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permiso = ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            )
            if (permiso != PackageManager.PERMISSION_GRANTED) {
                notificacionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    var nuevoPeso by remember { mutableStateOf("") }
    val snackbarActivo = viewModel.snackbarActivo

    LaunchedEffect(snackbarActivo) {
        if (snackbarActivo) {
            scope.launch {
                snackbarHostState.showSnackbar(viewModel.snackbarMensaje)
                viewModel.resetSnackbar()
                nuevoPeso = ""
                viewModel.cargarDatosUsuarioCompleto()
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nuevoPeso,
                onValueChange = { nuevoPeso = it },
                label = { Text("Nuevo peso (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.actualizarPeso(context, nuevoPeso.toFloatOrNull())
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}
