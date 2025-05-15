package com.tusalud.healthapp.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.tusalud.healthapp.R

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
                navController.navigate("main")
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