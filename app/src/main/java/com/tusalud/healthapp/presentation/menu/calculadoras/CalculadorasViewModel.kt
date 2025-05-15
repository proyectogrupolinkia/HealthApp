package com.tusalud.healthapp.presentation.menu.calculadoras

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log10

class CalculadorasViewModel : ViewModel() {

    var peso by mutableStateOf("")
    var altura by mutableStateOf("")
    var cintura by mutableStateOf("")
    var cuello by mutableStateOf("")
    var cadera by mutableStateOf("")
    var esHombre by mutableStateOf(true)

    var imc by mutableStateOf<Float?>(null)
    var grasaCorporal by mutableStateOf<Float?>(null)

    fun calcularIMC(): Float? {
        val p = peso.toFloatOrNull()
        val a = altura.toFloatOrNull()
        return if (p != null && a != null && a > 0f) {
            val resultado = p / (a * a)
            imc = resultado
            resultado
        } else {
            null
        }
    }

    fun calcularGrasaCorporal(): Float? {
        val a = altura.toFloatOrNull()
        val c = cintura.toFloatOrNull()
        val cu = cuello.toFloatOrNull()
        val ca = cadera.toFloatOrNull()

        if (a == null || a <= 0f || c == null || cu == null) return null
        if (!esHombre && (ca == null || ca <= 0f)) return null

        // Comprobar que las medidas tengan sentido para evitar errores en log10
        if (c <= cu) return null
        if (!esHombre && ca != null && (c + ca) <= cu) return null

        val alturaCm = a * 100f

        val resultado = if (esHombre) {
            495f / (1.0324f - 0.19077f * log10((c - cu).toDouble()) + 0.15456f * log10(alturaCm.toDouble())).toFloat() - 450f
        } else {
            495f / (1.29579f - 0.35004f * log10((c + ca!! - cu).toDouble()) + 0.221f * log10(alturaCm.toDouble())).toFloat() - 450f
        }
        grasaCorporal = resultado
        return resultado
    }

    fun guardarDatosSiValidos() {
        val imcVal = imc
        val grasaVal = grasaCorporal
        if (imcVal != null && grasaVal != null) {
            val user = FirebaseAuth.getInstance().currentUser
            val db = FirebaseFirestore.getInstance()

            user?.let {
                val datos = hashMapOf(
                    "imc" to imcVal,
                    "grasaCorporal" to grasaVal
                )
                db.collection("usuarios").document(user.uid).set(datos)
            }
        }
    }
}


