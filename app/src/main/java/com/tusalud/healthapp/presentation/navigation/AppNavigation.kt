package com.tusalud.healthapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tusalud.healthapp.presentation.login.LoginScreen
import com.tusalud.healthapp.presentation.login.LoginViewModel
import com.tusalud.healthapp.presentation.register.RegisterScreen
import com.tusalud.healthapp.presentation.reset.PasswordResetScreen
import com.tusalud.healthapp.presentation.main.MainScreen
import com.tusalud.healthapp.presentation.menu.calculadoras.CalculadorasScreen
import com.tusalud.healthapp.presentation.menu.calculadoras.CalculadorasViewModel
import com.tusalud.healthapp.presentation.menu.desafios.DesafiosScreen
import com.tusalud.healthapp.presentation.menu.desafios.DesafiosViewModel
import com.tusalud.healthapp.presentation.menu.progress.ProgressScreen
import com.tusalud.healthapp.presentation.menu.progress.ProgressViewModel
import com.tusalud.healthapp.presentation.menu.progress.perfil.PerfilScreen
import com.tusalud.healthapp.presentation.menu.progress.perfil.PerfilViewModel
import com.tusalud.healthapp.presentation.menu.progress.perfil.EditarPerfilScreen
import com.tusalud.healthapp.presentation.menu.progress.perfil.EditarPerfilViewModel
import com.tusalud.healthapp.presentation.menu.progress.recordatorios.RecordatoriosScreen
import com.tusalud.healthapp.presentation.menu.progress.recordatorios.RecordatoriosViewModel
import com.tusalud.healthapp.presentation.menu.progress.configuracion.ConfiguracionScreen
import com.tusalud.healthapp.presentation.menu.progress.configuracion.ConfiguracionViewModel
import com.tusalud.healthapp.presentation.menu.progress.peso.ActualizarPesoScreen
import com.tusalud.healthapp.presentation.menu.progress.peso.ActualizarPesoViewModel
import com.tusalud.healthapp.presentation.menu.progress.peso.EvolucionPesoScreen
import com.tusalud.healthapp.presentation.menu.progress.peso.EvolucionPesoViewModel

@Composable
fun AppNavigation(navController: NavHostController) {

    val progressViewModel: ProgressViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, viewModel)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("reset") {
            PasswordResetScreen(navController)
        }

        composable("main") {
            MainScreen(navController)
        }

        composable("perfil") {
            val viewModel: PerfilViewModel = hiltViewModel()
            PerfilScreen(navController, viewModel)
        }

        composable("editar_perfil") {
            val viewModel: EditarPerfilViewModel = hiltViewModel()
            EditarPerfilScreen(navController, viewModel)
        }

        composable("recordatorios") {
            val viewModel: RecordatoriosViewModel = hiltViewModel()
            RecordatoriosScreen(navController, viewModel)
        }

        composable("configuracion") {
            val viewModel: ConfiguracionViewModel = hiltViewModel()
            ConfiguracionScreen(navController, viewModel)
        }

        composable("desafio") {
            val viewModel: DesafiosViewModel = hiltViewModel()
            DesafiosScreen(navController, viewModel)
        }

        composable("calculadoras") {
            val viewModel: CalculadorasViewModel = hiltViewModel()
            CalculadorasScreen(navController, viewModel)
        }

        composable("progress") {
            ProgressScreen(navController, progressViewModel)
        }

        composable("actualizar_peso") {
            val viewModel: ActualizarPesoViewModel = hiltViewModel()
            ActualizarPesoScreen(navController, viewModel)
        }

        composable("evolucion_peso") {
            val viewModel: EvolucionPesoViewModel = hiltViewModel()
            EvolucionPesoScreen(navController, viewModel)
        }
    }
}
