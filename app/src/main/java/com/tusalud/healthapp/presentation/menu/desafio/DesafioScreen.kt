package com.tusalud.healthapp.presentation.menu.desafio

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesafioScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { TextoAnimado ("Desafios")},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Aquí iría la información detallada del desafío.")
        }
    }
}

/** Función que recibo un string y muestra una animación básica con dicho string
 * @param word String que se quiere animar
 * @author Alejandro
 * */
@Composable
fun TextoAnimado(word: String) : String {
    val infiniteTransition = rememberInfiniteTransition()

    Row {
        word.forEachIndexed { index, char ->
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 500 + index * 100, // Retraso para cada letra
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
            Text(
                text = char.toString(),
                fontSize = 30.sp,
                color = Color.Green.copy(alpha = alpha),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
    return word
}