package com.tusalud.healthapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tusalud.healthapp.presentation.login.LoginScreen
import com.tusalud.healthapp.presentation.login.LoginViewModel
import com.tusalud.healthapp.presentation.main.MainScreen
import com.tusalud.healthapp.presentation.register.RegisterScreen
import com.tusalud.healthapp.presentation.reset.PasswordResetScreen

@Composable
fun AppNavigation(viewModel: LoginViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(viewModel, navController) }
        composable("register") { RegisterScreen(viewModel, navController) }
        composable("reset") { PasswordResetScreen(viewModel, navController) }
        composable("main") { viewModel.user?.let { MainScreen(it) } }
    }
}
