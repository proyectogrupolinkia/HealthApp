package com.tusalud.healthapp.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tusalud.healthapp.presentation.components.BottomNavigationBar
import com.tusalud.healthapp.presentation.menu.calculadoras.CalculadorasScreen
import com.tusalud.healthapp.presentation.menu.progress.perfil.PerfilScreen
import com.tusalud.healthapp.presentation.menu.progress.ProgressScreen

@Composable
fun MainScreen(
    navController: NavHostController,
    startTab: Int = 0, // Índice de la pestaña por defecto (0: Progreso)
) {

    //revisa el parametro tab
    val currentEntry = navController.currentBackStackEntryAsState().value
    val tabArg = currentEntry?.arguments?.getString("tab")?.toIntOrNull()

    // Estado para controlar la pestaña seleccionada

    var selectedTab by rememberSaveable { mutableStateOf(tabArg ?: startTab) }

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
            // Muestra una de las tres pantallas según la pestaña seleccionada


            when (selectedTab) {
                0 -> ProgressScreen(navController = navController)
                1 -> CalculadorasScreen(navController = navController)
                2 -> PerfilScreen(navController = navController)
            }
        }
    }
}
