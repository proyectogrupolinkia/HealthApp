package com.tusalud.healthapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tusalud.healthapp.presentation.login.LoginScreen
import com.tusalud.healthapp.presentation.register.RegisterScreen
import com.tusalud.healthapp.presentation.reset.PasswordResetScreen
import com.tusalud.healthapp.presentation.main.MainScreen
import com.tusalud.healthapp.presentation.menu.CalculadorasScreen
import com.tusalud.healthapp.presentation.menu.PerfilScreen
import com.tusalud.healthapp.presentation.menu.EditarPerfilScreen
import com.tusalud.healthapp.presentation.menu.RecordatoriosScreen
import com.tusalud.healthapp.presentation.menu.ConfiguracionScreen
import com.tusalud.healthapp.presentation.menu.desafio.DesafioScreen
import com.tusalud.healthapp.presentation.menu.Progress.ProgressScreen
import com.tusalud.healthapp.presentation.menu.Progress.ProgressViewModel
import com.tusalud.healthapp.presentation.menu.Progress.ActualizarPesoScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    val progressViewModel: ProgressViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("reset_password") {
            PasswordResetScreen(navController)
        }

        composable("main") {
            MainScreen(navController)
        }

        composable("perfil") {
            PerfilScreen(navController)
        }

        composable("editar_perfil") {
            EditarPerfilScreen(navController)
        }

        composable("recordatorios") {
            RecordatoriosScreen(navController)
        }

        composable("configuracion") {
            ConfiguracionScreen(navController)
        }

        composable("desafio") {
            DesafioScreen(navController)
        }

        composable("calculadoras") {
            CalculadorasScreen(navController)
        }

        composable("progress") {
            ProgressScreen(navController, progressViewModel)
        }

        composable("actualizar_peso") {
            ActualizarPesoScreen(navController, progressViewModel)
        }
    }
}