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
import com.tusalud.healthapp.presentation.menu.progress.ProgressViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditarPerfilScreen(
    navController: NavHostController,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val displayName by viewModel.displayName.collectAsState()
    val email by viewModel.email.collectAsState()
    val pesoInicio by viewModel.pesoInicio.collectAsState()
    val pesoObjetivo by viewModel.pesoObjetivo.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.cargarDatosUsuarioCompleto()
        viewModel.toastMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text("Editar perfil", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = displayName,
            onValueChange = { viewModel.onDisplayNameChanged(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = pesoInicio,
            onValueChange = { viewModel.onPesoInicioChanged(it) },
            label = { Text("Peso de inicio (kg)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = pesoObjetivo,
            onValueChange = { viewModel.onPesoObjetivoChanged(it) },
            label = { Text("Peso objetivo (kg)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.updateProfile {
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar cambios")
        }
    }
}