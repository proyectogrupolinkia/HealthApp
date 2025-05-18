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
import com.tusalud.healthapp.presentation.menu.Progress.peso.ActualizarPesoScreen

import com.tusalud.healthapp.presentation.menu.calculadoras.CalculadorasScreen
import com.tusalud.healthapp.presentation.menu.calculadoras.CalculadorasViewModel
import com.tusalud.healthapp.presentation.menu.desafios.DesafiosScreen
import com.tusalud.healthapp.presentation.menu.desafios.DesafiosViewModel
import com.tusalud.healthapp.presentation.menu.meditacion.MeditacionScreen
import com.tusalud.healthapp.presentation.menu.meditacion.MeditacionViewModel
import com.tusalud.healthapp.presentation.main.MainViewModel
import com.tusalud.healthapp.presentation.menu.progress.configuracion.ConfiguracionScreen
import com.tusalud.healthapp.presentation.menu.progress.configuracion.ConfiguracionViewModel
import com.tusalud.healthapp.presentation.menu.progress.peso.EvolucionPesoScreen
import com.tusalud.healthapp.presentation.menu.progress.perfil.EditarPerfilScreen
import com.tusalud.healthapp.presentation.menu.progress.perfil.EditarPerfilViewModel
import com.tusalud.healthapp.presentation.menu.progress.recordatorios.RecordatoriosScreen
import com.tusalud.healthapp.presentation.menu.progress.recordatorios.RecordatoriosViewModel

@Composable
fun AppNavigation(navController: NavHostController) {

    // Inyección centralizada de todos los ViewModels
    val loginViewModel: LoginViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val editarPerfilViewModel: EditarPerfilViewModel = hiltViewModel()
    val recordatoriosViewModel: RecordatoriosViewModel = hiltViewModel()
    val configuracionViewModel: ConfiguracionViewModel = hiltViewModel()
    val desafioViewModel: DesafiosViewModel = hiltViewModel()
    val calculadorasViewModel: CalculadorasViewModel = hiltViewModel()
    val meditacionViewModel: MeditacionViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController, loginViewModel)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("reset") {
            PasswordResetScreen(navController)
        }

        composable("main") {
            MainScreen(navController, mainViewModel)
        }

        composable("editar_perfil") {
            EditarPerfilScreen(navController, editarPerfilViewModel, mainViewModel)
        }

        composable("recordatorios") {
            RecordatoriosScreen(navController, recordatoriosViewModel)
        }

        composable("configuracion") {
            ConfiguracionScreen(navController, configuracionViewModel)
        }

        composable("desafio") {
            DesafiosScreen(navController, desafioViewModel)
        }

        composable("calculadoras") {
            CalculadorasScreen(navController, calculadorasViewModel)
        }

        composable("actualizar_peso") {
            ActualizarPesoScreen(navController, mainViewModel)
        }

        composable("evolucion_peso") {
            EvolucionPesoScreen(navController, mainViewModel)
        }

        composable("meditacion") {
            MeditacionScreen(navController, meditacionViewModel)
        }
    }
}