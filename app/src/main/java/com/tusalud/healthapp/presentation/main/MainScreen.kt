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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tusalud.healthapp.R
import com.tusalud.healthapp.presentation.menu.CalculadorasScreen


import com.tusalud.healthapp.presentation.menu.PerfilScreen
import com.tusalud.healthapp.presentation.menu.Progress.ProgressScreen
import com.tusalud.healthapp.presentation.menu.Progress.ProgressViewModel


@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                navController = navController
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> ProgressScreen(navController, viewModel)
                1 -> CalculadorasScreen(navController)
                2 -> PerfilScreen(navController)
            }
        }
    }

}
@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    navController: NavHostController
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = {
                onTabSelected(0)
                navController.navigate("main") // O NavigationRoutes.MAIN
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_progreso),
                    contentDescription = "Progreso"
                )
            },
            label = { Text("Progreso") }
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = {
                onTabSelected(1)
                navController.navigate("calculadoras")
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calculadora),
                    contentDescription = "Calculadoras"
                )
            },
            label = { Text("Calculadoras") }
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = {
                onTabSelected(2)
                navController.navigate("perfil")
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_perfil),
                    contentDescription = "Perfil"
                )
            },
            label = { Text("Perfil") }
        )
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
