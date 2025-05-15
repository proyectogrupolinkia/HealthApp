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
                if (selectedTab != 0) {
                    onTabSelected(0)
                    navController.navigate("main") {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
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
                if (selectedTab != 1) {
                    onTabSelected(1)
                    navController.navigate("calculadoras") {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
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
                if (selectedTab != 2) {
                    onTabSelected(2)
                    navController.navigate("perfil") {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
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
