package com.tusalud.healthapp.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tusalud.healthapp.presentation.components.BottomNavigationBar
import com.tusalud.healthapp.presentation.menu.calculadoras.CalculadorasScreen
import com.tusalud.healthapp.presentation.menu.progress.perfil.PerfilScreen
import com.tusalud.healthapp.presentation.menu.progress.ProgressScreen
import com.tusalud.healthapp.presentation.menu.progress.ProgressViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    startTab: Int = 0, // Nuevo parámetro para seleccionar el tab inicial
    viewModel: ProgressViewModel = hiltViewModel()
) {
    // Leer la ruta actual para obtener el parámetro "tab" si existe
    val currentEntry = navController.currentBackStackEntryAsState().value
    val tabArg = currentEntry?.arguments?.getString("tab")?.toIntOrNull()
    var selectedTab by remember { mutableStateOf(tabArg ?: startTab) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                navController = navController
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                0 -> ProgressScreen(navController = navController, viewModel = viewModel)
                1 -> CalculadorasScreen(navController = navController)
                2 -> PerfilScreen(navController = navController)
            }
        }
    }
}
@Composable
fun ProgressInfoCard(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .padding(16.dp)
    ) {
        Text(text = title, color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}


