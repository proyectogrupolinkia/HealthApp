package com.tusalud.healthapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tusalud.healthapp.presentation.login.LoginScreen
import com.tusalud.healthapp.presentation.main.MainScreen
import com.tusalud.healthapp.presentation.menu.CalculadorasScreen
import com.tusalud.healthapp.presentation.menu.desafio.DesafioScreen
import com.tusalud.healthapp.presentation.menu.PerfilScreen
import com.tusalud.healthapp.presentation.menu.EditarPerfilScreen
import com.tusalud.healthapp.presentation.menu.RecordatoriosScreen
import com.tusalud.healthapp.presentation.menu.ConfiguracionScreen
import com.tusalud.healthapp.presentation.register.RegisterScreen
import com.tusalud.healthapp.presentation.reset.PasswordResetScreen



@Composable
fun AppNavigation(navController: NavHostController) {
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

        composable(NavigationRoutes.CALCULADORAS) {
            CalculadorasScreen(navController)
        }

        composable("perfil") {
            PerfilScreen(navController = navController)
        }

        composable("editarPerfil") {  //pantalla de editar perfil
            EditarPerfilScreen(navController)
        }

        composable("recordatorios") {  //pantalla de recordatorios
            RecordatoriosScreen(navController)
        }

        composable("configuracion") {  //pantalla de configuracion
            ConfiguracionScreen(navController)
        }

        composable(NavigationRoutes.DESAFIO) {
            DesafioScreen(navController)
        }


    }
}

