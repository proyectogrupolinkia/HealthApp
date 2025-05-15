package com.tusalud.healthapp.presentation.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tusalud.healthapp.presentation.components.BottomNavigationBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlin.math.log10
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavHostController


@Composable
fun CalculadorasScreen(navController: NavHostController){

    var selectedTab by remember { mutableStateOf(1) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                navController = navController
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            var peso by remember { mutableStateOf("") }
            var altura by remember { mutableStateOf("") }
            var cintura by remember { mutableStateOf("") }
            var cuello by remember { mutableStateOf("") }
            var cadera by remember { mutableStateOf("") }
            var esHombre by remember { mutableStateOf(true) }

            var imc by remember { mutableStateOf<Float?>(null) }
            var grasaCorporal by remember { mutableStateOf<Float?>(null) }

            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Calculadora de Salud",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF00C6A7)
                    )

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = peso,
                                onValueChange = { peso = it },
                                label = { Text("Peso (kg)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = altura,
                                onValueChange = { altura = it },
                                label = { Text("Altura (cm)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = cintura,
                                onValueChange = { cintura = it },
                                label = { Text("Cintura (cm)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = cuello,
                                onValueChange = { cuello = it },
                                label = { Text("Cuello (cm)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (!esHombre) {
                                OutlinedTextField(
                                    value = cadera,
                                    onValueChange = { cadera = it },
                                    label = { Text("Cadera (cm)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Sexo:")
                                RadioButton(selected = esHombre, onClick = { esHombre = true })
                                Text("Hombre")
                                RadioButton(selected = !esHombre, onClick = { esHombre = false })
                                Text("Mujer")
                            }

                            Button(
                                onClick = {
                                    val p = peso.toFloatOrNull()
                                    val a = altura.toFloatOrNull()?.div(100f)
                                    val c = cintura.toFloatOrNull()
                                    val cu = cuello.toFloatOrNull()
                                    val ca = cadera.toFloatOrNull()

                                    if (p != null && a != null && a > 0f && c != null && cu != null && (esHombre || ca != null)) {
                                        imc = calcularIMC(p, a)
                                        grasaCorporal = calcularGrasaCorporal(c, cu, a * 100f, ca, esHombre)
                                        guardarDatosSalud(imc!!, grasaCorporal!!)

                                        scope.launch {
                                            snackbarHostState.showSnackbar("Datos guardados correctamente")
                                        }
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Por favor completa todos los campos correctamente")
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C6A7))
                            ) {
                                Text("Calcular y Guardar", color = Color.White)
                            }
                        }
                    }

                    // Mostrar resultados
                    imc?.let {
                        Text("IMC: %.2f".format(it), style = MaterialTheme.typography.bodyLarge)
                    }
                    grasaCorporal?.let {
                        Text("Grasa Corporal: %.2f%%".format(it), style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

// Cálculo del IMC
fun calcularIMC(peso: Float, altura: Float): Float {
    return peso / (altura * altura)
}

// Cálculo de grasa corporal
fun calcularGrasaCorporal(
    cintura: Float,
    cuello: Float,
    altura: Float,
    cadera: Float?,
    esHombre: Boolean
): Float {
    return if (esHombre) {
        495f / (1.0324f - 0.19077f * log10((cintura - cuello).toDouble()) + 0.15456f * log10(altura.toDouble())).toFloat() - 450f
    } else {
        495f / (1.29579f - 0.35004f * log10((cintura + (cadera ?: 0f) - cuello).toDouble()) + 0.221f * log10(altura.toDouble())).toFloat() - 450f
    }
}

// Guardado en Firestore
fun guardarDatosSalud(imc: Float, grasa: Float) {
    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    user?.let {
        val datos = hashMapOf(
            "imc" to imc,
            "grasaCorporal" to grasa
        )
        db.collection("usuarios").document(user.uid).set(datos)
    }
}