package com.tusalud.healthapp.presentation.menu.calculadoras

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.animation.AnimatedVisibility
import com.tusalud.healthapp.R
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.clip


@Composable
fun CalculadorasScreen(navController: NavHostController, viewModel: CalculadorasViewModel = viewModel()) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val backgroundColor = Color(0xFFFFE0B2)

    Scaffold(
        containerColor = backgroundColor,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //barra drag
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.LightGray)
                )
            }

            Text(
                text = "Calculadoras",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF8066)),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // campos base
                    listOf(
                        "Peso (kg)" to viewModel.peso,
                        "Altura (m)" to viewModel.altura,
                        "Cintura (cm)" to viewModel.cintura,
                        "Cuello (cm)" to viewModel.cuello
                    ).forEach { (label, value) ->
                        OutlinedTextField(
                            value = value,
                            onValueChange = { newValue ->
                                when (label) {
                                    "Peso (kg)" -> viewModel.peso = newValue
                                    "Altura (m)" -> viewModel.altura = newValue
                                    "Cintura (cm)" -> viewModel.cintura = newValue
                                    "Cuello (cm)" -> viewModel.cuello = newValue
                                }
                            },
                            label = { Text(label) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // campo cadera animado
                    AnimatedVisibility(visible = !viewModel.esHombre) {
                        OutlinedTextField(
                            value = viewModel.cadera,
                            onValueChange = { viewModel.cadera = it },
                            label = { Text("Cadera (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // switch selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Hombre", color = Color.White)
                        Spacer(Modifier.width(12.dp))
                        Switch(
                            checked = !viewModel.esHombre,
                            onCheckedChange = { viewModel.esHombre = !it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF009DFF),
                                uncheckedThumbColor = Color.White
                            )
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("Mujer", color = Color.White)
                    }

                    // botones
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                val resultadoIMC = viewModel.calcularIMC()
                                scope.launch {
                                    if (resultadoIMC == null) {
                                        snackbarHostState.showSnackbar("Por favor ingresa peso y altura válidos")
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009DFF))
                        ) {
                            Text("Calcular IMC", color = Color.White)
                        }

                        Button(
                            onClick = {
                                val resultadoGrasa = viewModel.calcularGrasaCorporal()
                                scope.launch {
                                    if (resultadoGrasa == null) {
                                        snackbarHostState.showSnackbar("Por favor completa todos los campos correctamente")
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C6A7))
                        ) {
                            Text("Grasa Corporal", color = Color.White)
                        }
                    }

                    // resultados + imagen juntos
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // resultados visibles solo si hay valores
                        AnimatedVisibility(visible = viewModel.imc != null || viewModel.grasaCorporal != null) {
                            Column {
                                viewModel.imc?.let {
                                    Text(
                                        "IMC: %.2f".format(it),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Black
                                    )
                                }

                                viewModel.grasaCorporal?.let {
                                    Text(
                                        "Grasa: %.2f%%".format(it),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Black
                                    )
                                }
                            }
                        }

                        //imagen
                        Image(
                            painter = painterResource(id = R.drawable.hombre_pesandose),
                            contentDescription = "Hombre pesándose",
                            modifier = Modifier.size(150.dp)
                        )
                    }

                }
            }
        }
    }
}



