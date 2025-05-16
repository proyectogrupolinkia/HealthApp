package com.tusalud.healthapp.presentation.menu.Progress

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
import com.tusalud.healthapp.presentation.menu.progress.ProgressViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualizarPesoScreen(
    navController: NavHostController,
    viewModel: ProgressViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var nuevoPeso by remember { mutableStateOf("") }
    val snackbarActivo = viewModel.snackbarActivo

    // Recargar datos al volver, para actualizar gráfico o vista principal
    LaunchedEffect(snackbarActivo) {
        if (snackbarActivo) {
            scope.launch {
                snackbarHostState.showSnackbar("Peso guardado correctamente")
                viewModel.resetSnackbar()
                nuevoPeso = ""
                // Refrescar datos en el ViewModel
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
                    viewModel.actualizarPeso(nuevoPeso.toFloatOrNull())
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}