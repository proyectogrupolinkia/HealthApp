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
import com.tusalud.healthapp.presentation.menu.progress.peso.ActualizarPesoScreen
import com.tusalud.healthapp.presentation.menu.calculadoras.CalculadorasViewModel
import com.tusalud.healthapp.presentation.menu.calculadoras.CalculadorasScreen
import com.tusalud.healthapp.presentation.menu.desafios.DesafiosScreen
import com.tusalud.healthapp.presentation.menu.desafios.DesafiosViewModel
import com.tusalud.healthapp.presentation.menu.meditacion.MeditacionScreen
import com.tusalud.healthapp.presentation.menu.meditacion.MeditacionViewModel
import com.tusalud.healthapp.presentation.menu.progress.ProgressViewModel
import com.tusalud.healthapp.presentation.menu.progress.ProgressScreen
import com.tusalud.healthapp.presentation.menu.progress.configuracion.ConfiguracionScreen
import com.tusalud.healthapp.presentation.menu.progress.configuracion.ConfiguracionViewModel
import com.tusalud.healthapp.presentation.menu.progress.peso.EvolucionPesoScreen
import com.tusalud.healthapp.presentation.menu.progress.perfil.PerfilScreen
import com.tusalud.healthapp.presentation.menu.progress.perfil.PerfilViewModel
import com.tusalud.healthapp.presentation.menu.progress.perfil.EditarPerfilScreen
import com.tusalud.healthapp.presentation.menu.progress.perfil.EditarPerfilViewModel
import com.tusalud.healthapp.presentation.menu.progress.recordatorios.RecordatoriosScreen
import com.tusalud.healthapp.presentation.menu.progress.recordatorios.RecordatoriosViewModel

@Composable
fun AppNavigation(navController: NavHostController) {

    // ViewModels compartidos
    val progressViewModel: ProgressViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {

        // Flujo de autenticación
        composable("login") {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("reset") {
            PasswordResetScreen(navController)
        }

        // Contenedor principal con BottomNavigationBar
        composable("main") {
            MainScreen(navController)
        }

        composable("main?tab={tab}") { backStackEntry ->
            val tabIndex = backStackEntry.arguments?.getString("tab")?.toIntOrNull() ?: 0
            MainScreen(navController, startTab = tabIndex)
        }

        // Pantallas fuera del bottom bar (accedidas desde botones internos)

        composable("progreso") {
            val progressViewModel: ProgressViewModel = hiltViewModel()
            ProgressScreen(navController, progressViewModel)
        }

        composable("perfil") {
            val perfilViewModel: PerfilViewModel = hiltViewModel()
            PerfilScreen(navController, perfilViewModel)
        }
        composable("editar_perfil") {
            val editarPerfilViewModel: EditarPerfilViewModel = hiltViewModel()
            EditarPerfilScreen(navController, progressViewModel)
        }

        composable("recordatorios") {
            val recordatoriosViewModel: RecordatoriosViewModel = hiltViewModel()
            RecordatoriosScreen(navController, recordatoriosViewModel)
        }

        composable("configuracion") {
            val configuracionViewModel: ConfiguracionViewModel = hiltViewModel()
            ConfiguracionScreen(navController, configuracionViewModel)
        }

        composable("desafio") {
            val desafioViewModel: DesafiosViewModel = hiltViewModel()
            DesafiosScreen(navController, desafioViewModel)
        }

        composable("calculadoras") {
            val calculadorasViewModel: CalculadorasViewModel = hiltViewModel()
            CalculadorasScreen(navController, calculadorasViewModel)
        }

        composable("actualizar_peso") {
            ActualizarPesoScreen(navController, progressViewModel)
        }

        composable("evolucion_peso") {
            EvolucionPesoScreen(navController)
        }
        composable("meditacion") {
            val meditacionViewModel: MeditacionViewModel = hiltViewModel()
            MeditacionScreen(navController, meditacionViewModel)
        }

    }
}
