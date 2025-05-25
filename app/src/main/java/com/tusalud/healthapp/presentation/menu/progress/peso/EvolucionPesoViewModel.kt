package com.tusalud.healthapp.presentation.menu.progress.peso

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EvolucionPesoViewModel @Inject constructor() : ViewModel() {

    private val _pesosConFechas = MutableStateFlow<List<Pair<Float, String>>>(emptyList())
    val pesosConFechas: StateFlow<List<Pair<Float, String>>> = _pesosConFechas

    private val _pesoObjetivo = MutableStateFlow("")
    val pesoObjetivo: StateFlow<String> = _pesoObjetivo

    fun cargarDatosEvolucion() {
        // Limpiar antes de cargar
        _pesosConFechas.value = emptyList()

        val user = FirebaseAuth.getInstance().currentUser ?: return
        val uid = user.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                val historialRaw = document.get("weightHistory") as? List<*>
                val historial = historialRaw?.filterIsInstance<Map<String, Any>>() ?: emptyList()
                val formato = SimpleDateFormat("dd MMM", Locale.getDefault())

                _pesosConFechas.value = historial.mapNotNull { item ->
                    val peso = (item["peso"] as? Number)?.toFloat()
                    val fecha = (item["timestamp"] as? Timestamp)?.toDate()
                    if (peso != null && fecha != null) peso to formato.format(fecha) else null
                }

                _pesoObjetivo.value = document.getDouble("pesoObjetivo")?.toString() ?: ""
            }
            .addOnFailureListener {
                _pesosConFechas.value = emptyList()
            }
    }
}
