package com.tusalud.healthapp.presentation.menu.progress.perfil

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tusalud.healthapp.presentation.main.MainViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditarPerfilScreen(
    navController: NavHostController,
    editarPerfilViewModel: EditarPerfilViewModel,
    viewModel: MainViewModel = hiltViewModel()
) {
    val displayName by viewModel.displayName.collectAsState()
    val pesoInicio by viewModel.pesoInicio.collectAsState()
    val pesoObjetivo by viewModel.pesoObjetivo.collectAsState()
    val edad by editarPerfilViewModel.edad.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.cargarDatosUsuarioCompleto()
        viewModel.toastMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val edadError = !editarPerfilViewModel.isEdadValida()
    val pesoInicioError = !editarPerfilViewModel.isPesoValido(pesoInicio)
    val pesoObjetivoError = !editarPerfilViewModel.isPesoValido(pesoObjetivo)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                navController.navigate("main?tab=2") {
                    popUpTo("main") { inclusive = true }
                }
            }
        ) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Editar perfil", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = displayName,
            onValueChange = { viewModel.onDisplayNameChanged(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = edad,
            onValueChange = { editarPerfilViewModel.onEdadChanged(it) },
            label = { Text("Edad") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = edadError
        )
        if (edadError) {
            Text(
                text = "Edad válida entre 1 y 120",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = pesoInicio,
            onValueChange = { viewModel.onPesoInicioChanged(it) },
            label = { Text("Peso de inicio (kg)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = pesoInicioError
        )
        if (pesoInicioError) {
            Text(
                text = "Peso válido entre 20.0 y 500.0 (usar punto)",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = pesoObjetivo,
            onValueChange = { viewModel.onPesoObjetivoChanged(it) },
            label = { Text("Peso objetivo (kg)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = pesoObjetivoError
        )
        if (pesoObjetivoError) {
            Text(
                text = "Peso válido entre 20.0 y 500.0 (usar punto)",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.updateProfile {
                    editarPerfilViewModel.updateProfile()
                    navController.navigate("main?tab=2") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = editarPerfilViewModel.isFormValid(pesoInicio, pesoObjetivo)
        ) {
            Text("Guardar cambios")
        }
    }
}


