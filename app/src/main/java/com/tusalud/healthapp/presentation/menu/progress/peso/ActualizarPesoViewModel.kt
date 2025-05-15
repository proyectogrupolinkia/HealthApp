package com.tusalud.healthapp.presentation.menu.progress.peso

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ActualizarPesoViewModel : ViewModel() {

    private val _snackbarActivo = MutableStateFlow(false)
    val snackbarActivo: StateFlow<Boolean> = _snackbarActivo

    private val _snackbarError = MutableStateFlow<String?>(null)
    val snackbarError: StateFlow<String?> = _snackbarError

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun actualizarPeso(peso: Float?) {
        if (peso == null || peso <= 0f) {
            _snackbarError.value = "Peso inválido"
            return
        }

        val user = auth.currentUser
        if (user == null) {
            _snackbarError.value = "Usuario no autenticado"
            return
        }

        viewModelScope.launch {
            val datos = hashMapOf(
                "peso" to peso,
                "timestamp" to System.currentTimeMillis()
            )
            db.collection("usuarios")
                .document(user.uid)
                .collection("pesos")
                .add(datos)
                .addOnSuccessListener {
                    _snackbarActivo.value = true
                }
                .addOnFailureListener { e ->
                    _snackbarError.value = "Error al guardar peso: ${e.message}"
                }
        }
    }

    fun resetSnackbar() {
        _snackbarActivo.value = false
    }

    fun resetSnackbarError() {
        _snackbarError.value = null
    }
}


