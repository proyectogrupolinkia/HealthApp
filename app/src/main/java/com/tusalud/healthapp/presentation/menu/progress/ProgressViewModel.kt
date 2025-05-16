package com.tusalud.healthapp.presentation.menu.progress

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tusalud.healthapp.domain.model.Progress
import com.tusalud.healthapp.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _progress = MutableStateFlow<Progress?>(null)
    val progress: StateFlow<Progress?> = _progress

    private val _pesos = MutableStateFlow<List<Float>>(emptyList())
    val pesos: StateFlow<List<Float>> = _pesos

    private val _pesosConFechas = MutableStateFlow<List<Pair<Float, String>>>(emptyList())
    val pesosConFechas: StateFlow<List<Pair<Float, String>>> = _pesosConFechas

    var snackbarActivo by mutableStateOf(false)
        private set

    init {
        loadProgress()
        cargarPesosDesdeFirebase()
    }

    internal fun cargarPesosDesdeFirebase() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val listaRaw = document.get("weightHistory") as? List<*>
                val lista = listaRaw?.filterIsInstance<Map<String, Any>>()
                lista?.let {
                    val pesosFloat = it.mapNotNull { item ->
                        (item["peso"] as? Number)?.toFloat()
                    }
                    _pesos.value = pesosFloat

                    val formato = SimpleDateFormat("dd MMM", Locale.getDefault())
                    val pesosFechas = it.mapNotNull { item ->
                        val peso = (item["peso"] as? Number)?.toFloat()
                        val fecha = (item["timestamp"] as? Timestamp)?.toDate()
                        if (peso != null && fecha != null) peso to formato.format(fecha) else null
                    }
                    _pesosConFechas.value = pesosFechas
                }
            }
            .addOnFailureListener {
                _pesos.value = emptyList()
                _pesosConFechas.value = emptyList()
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
            val historialRaw = doc.get("weightHistory") as? List<*>
            val historial = historialRaw?.filterIsInstance<Map<String, Any>>() ?: emptyList()

            val hoy = Calendar.getInstance()
            val listaActualizada = historial.toMutableList()
            var actualizado = false

            for (i in historial.indices) {
                val item = historial[i]
                val fecha = (item["timestamp"] as? Timestamp)?.toDate() ?: continue
                val cal = Calendar.getInstance().apply { time = fecha }

                val esHoy = hoy.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                        hoy.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)

                if (esHoy) {
                    listaActualizada[i] = mapOf(
                        "peso" to nuevoPeso,
                        "timestamp" to Timestamp.now()
                    )
                    actualizado = true
                    break
                }
            }

            if (!actualizado) {
                listaActualizada.add(
                    mapOf(
                        "peso" to nuevoPeso,
                        "timestamp" to Timestamp.now()
                    )
                )
            }

            val nuevosDatos = mapOf(
                "peso" to nuevoPeso,
                "weightHistory" to listaActualizada
            )

            userRef.set(nuevosDatos, SetOptions.merge())
                .addOnSuccessListener {
                    snackbarActivo = true
                    loadProgress()
                    cargarPesosDesdeFirebase()
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