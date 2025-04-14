package com.tusalud.healthapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.tusalud.healthapp.presentation.login.LoginScreen
import com.tusalud.healthapp.presentation.login.LoginViewModel
import com.tusalud.healthapp.presentation.main.MainScreen
import com.tusalud.healthapp.presentation.navigation.AppNavigation
import com.tusalud.healthapp.ui.theme.HealthappTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loginViewModel: LoginViewModel = hiltViewModel()
            AppNavigation(viewModel = loginViewModel)
        }
    }
}