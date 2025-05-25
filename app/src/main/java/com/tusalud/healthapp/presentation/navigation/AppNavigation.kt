
// Define la navegación principal de la app usando Jetpack Navigation Compose.
// Crea un NavHost con una lista de rutas y pantallas correspondientes.


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
import com.tusalud.healthapp.presentation.menu.meditacion.MeditacionScreen
import com.tusalud.healthapp.presentation.menu.meditacion.MeditacionViewModel
import com.tusalud.healthapp.presentation.menu.progress.configuracion.ConfiguracionScreen
import com.tusalud.healthapp.presentation.menu.progress.configuracion.ConfiguracionViewModel
import com.tusalud.healthapp.presentation.menu.progress.peso.ActualizarPesoScreen
import com.tusalud.healthapp.presentation.menu.progress.peso.ActualizarPesoViewModel
import com.tusalud.healthapp.presentation.menu.progress.peso.EvolucionPesoScreen
import com.tusalud.healthapp.presentation.menu.progress.peso.EvolucionPesoViewModel
import com.tusalud.healthapp.presentation.menu.progress.perfil.EditarPerfilScreen
import com.tusalud.healthapp.presentation.menu.progress.recordatorios.RecordatoriosScreen
import com.tusalud.healthapp.presentation.menu.progress.recordatorios.RecordatoriosViewModel
import com.tusalud.healthapp.presentation.perfil.EditarPerfilViewModel


@Composable
fun AppNavigation(navController: NavHostController) {

    // ViewModels inyectados de maqnera centralizada

    val loginViewModel: LoginViewModel = hiltViewModel()
    val editarPerfilViewModel: EditarPerfilViewModel = hiltViewModel()
    val recordatoriosViewModel: RecordatoriosViewModel = hiltViewModel()
    val configuracionViewModel: ConfiguracionViewModel = hiltViewModel()
    val desafioViewModel: DesafiosViewModel = hiltViewModel()
    val calculadorasViewModel: CalculadorasViewModel = hiltViewModel()
    val meditacionViewModel: MeditacionViewModel = hiltViewModel()
    val actualizarPesoViewModel: ActualizarPesoViewModel = hiltViewModel()
    val evolucionPesoViewModel: EvolucionPesoViewModel = hiltViewModel()

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
            MainScreen(navController)
        }

        // Navegación con parámetro para que se abra la pestaña correspondiente

        composable("main?tab={tab}") { backStackEntry ->
            val tabIndex = backStackEntry.arguments?.getString("tab")?.toIntOrNull() ?: 0
            MainScreen(navController, startTab = tabIndex)
        }

        composable("editar_perfil") {
            EditarPerfilScreen(navController, editarPerfilViewModel)
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
            ActualizarPesoScreen(navController, actualizarPesoViewModel)
        }

        composable("evolucion_peso") {
            EvolucionPesoScreen(navController, evolucionPesoViewModel)
        }

        composable("meditacion") {
            MeditacionScreen(navController, meditacionViewModel)
        }
    }
}
