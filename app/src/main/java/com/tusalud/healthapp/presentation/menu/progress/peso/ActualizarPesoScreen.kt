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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualizarPesoScreen(
    navController: NavHostController,
    viewModel: ActualizarPesoViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var nuevoPeso by remember { mutableStateOf("") }
    val snackbarActivo by viewModel.snackbarActivo.collectAsState()
    val snackbarError by viewModel.snackbarError.collectAsState()

    LaunchedEffect(snackbarActivo) {
        if (snackbarActivo) {
            scope.launch {
                snackbarHostState.showSnackbar("Peso guardado correctamente")
                viewModel.resetSnackbar()
                nuevoPeso = "" //limpiar campo tras guardar
            }
        }
    }

    LaunchedEffect(snackbarError) {
        snackbarError?.let { errorMsg ->
            scope.launch {
                snackbarHostState.showSnackbar(errorMsg)
                viewModel.resetSnackbarError()
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

