package com.tusalud.healthapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tusalud.healthapp.presentation.login.LoginScreen
import com.tusalud.healthapp.presentation.main.MainScreen
import com.tusalud.healthapp.presentation.main.ProgressViewModel
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
    }
}
