package com.tusalud.healthapp.presentation.menu.calculadoras

import androidx.compose.foundation.Image
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
import com.tusalud.healthapp.R
import com.tusalud.healthapp.presentation.components.BottomNavigationBar
import kotlinx.coroutines.launch

@Composable
fun CalculadorasScreen(navController: NavHostController, viewModel: CalculadorasViewModel = viewModel()) {

    var selectedTab by remember { mutableStateOf(1) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val backgroundColor = Color(0xFFFFE0B2)

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                navController = navController
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Scroll con formulario y botones arriba
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Calculadoras",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFF8066)),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = viewModel.peso,
                            onValueChange = { viewModel.peso = it },
                            label = { Text("Peso (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = viewModel.altura,
                            onValueChange = { viewModel.altura = it },
                            label = { Text("Altura (m)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = viewModel.cintura,
                            onValueChange = { viewModel.cintura = it },
                            label = { Text("Cintura (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = viewModel.cuello,
                            onValueChange = { viewModel.cuello = it },
                            label = { Text("Cuello (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (!viewModel.esHombre) {
                            OutlinedTextField(
                                value = viewModel.cadera,
                                onValueChange = { viewModel.cadera = it },
                                label = { Text("Cadera (cm)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Hombre", color = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Switch(
                                checked = !viewModel.esHombre,
                                onCheckedChange = { viewModel.esHombre = !it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF009DFF),
                                    uncheckedThumbColor = Color.White
                                )
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Mujer", color = Color.White)
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                    }
                }
            }

            // Resultados abajo a la izquierda
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                viewModel.imc?.let {
                    Text(
                        "IMC: %.2f".format(it),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                }

                viewModel.grasaCorporal?.let {
                    Text(
                        "Grasa Corporal: %.2f%%".format(it),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                }
            }

            // Imagen abajo a la derecha, un poco más grande
            Image(
                painter = painterResource(id = R.drawable.hombre_pesandose),
                contentDescription = "Hombre pesándose",
                modifier = Modifier
                    .size(182.dp)
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}





