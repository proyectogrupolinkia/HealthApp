package com.tusalud.healthapp.presentation.menu.Progress

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tusalud.healthapp.domain.model.Progress
import com.tusalud.healthapp.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _progress = MutableStateFlow<Progress?>(null)
    val progress: StateFlow<Progress?> = _progress
    private val _pesos = MutableStateFlow<List<Float>>(emptyList())
    val pesos: StateFlow<List<Float>> = _pesos


    // Renombrado para evitar conflictos
    var snackbarActivo by mutableStateOf(false)
        private set

    init {
        loadProgress()
        cargarPesosDesdeFirebase()

    }

    private fun cargarPesosDesdeFirebase() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val lista = document.get("weightHistory") as? List<Number>
                lista?.let {
                    _pesos.value = it.map { peso -> peso.toFloat() }
                }
            }
            .addOnFailureListener {
                _pesos.value = emptyList()
            }
    }


    fun loadProgress() {
        viewModelScope.launch {
            _progress.value = progressRepository.getProgress()
        }
    }

    fun actualizarPeso(nuevoPeso: Float?) {
        if (nuevoPeso == null) return

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("usuarios").document(userId)

        userRef.get().addOnSuccessListener { doc ->
            val pesoAnterior = doc.getDouble("peso")?.toFloat()
            val historial = doc.get("weightHistory") as? List<Float> ?: emptyList()

            val nuevosDatos = mapOf(
                "peso" to nuevoPeso,
                "weightHistory" to historial + listOfNotNull(pesoAnterior)
            )

            userRef.set(nuevosDatos, SetOptions.merge())
                .addOnSuccessListener {
                    snackbarActivo = true
                    loadProgress()
                }
                .addOnFailureListener {
                    println("Error al actualizar peso: ${it.message}")
                }
        }.addOnFailureListener {
            println("Error al obtener documento del usuario: ${it.message}")
        }
    }

    fun resetSnackbar() {
        snackbarActivo = false
    }
}