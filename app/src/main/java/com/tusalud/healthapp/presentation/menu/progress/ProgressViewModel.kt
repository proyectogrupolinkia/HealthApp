package com.tusalud.healthapp.presentation.progress

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tusalud.healthapp.domain.model.Progress
import com.tusalud.healthapp.domain.use_case.GetProgressUseCase
import com.tusalud.healthapp.domain.use_case.GetWeightHistoryUseCase
import com.tusalud.healthapp.utils.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val getProgressUseCase: GetProgressUseCase,
    private val getWeightHistoryUseCase: GetWeightHistoryUseCase
) : ViewModel() {

    private val _progress = MutableStateFlow<Progress?>(null)
    val progress: StateFlow<Progress?> = _progress

    private val _pesos = MutableStateFlow<List<Float>>(emptyList())
    val pesos: StateFlow<List<Float>> = _pesos

    private val _pesosConFechas = MutableStateFlow<List<Pair<Float, String>>>(emptyList())
    val pesosConFechas: StateFlow<List<Pair<Float, String>>> = _pesosConFechas

    var snackbarActivo = MutableStateFlow(false)
    var snackbarMensaje = MutableStateFlow("")

    init {
        loadProgress()
        cargarPesosDesdeFirebase()
    }

    fun loadProgress() {
        viewModelScope.launch {
            val result = getProgressUseCase()
            result.onSuccess {
                _progress.value = it
            }
        }
    }

    fun cargarPesosDesdeFirebase() {
        viewModelScope.launch {
            val result = getWeightHistoryUseCase()
            result.onSuccess { (listaPesos, listaPesosConFechas) ->
                _pesos.value = listaPesos
                _pesosConFechas.value = listaPesosConFechas
            }
        }
    }


    fun actualizarPeso(context: Context, nuevoPeso: Float?) {
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
                    listaActualizada[i] = mapOf("peso" to nuevoPeso, "timestamp" to Timestamp.now())
                    actualizado = true
                    break
                }
            }
            if (!actualizado) {
                listaActualizada.add(mapOf("peso" to nuevoPeso, "timestamp" to Timestamp.now()))
            }
            val nuevosDatos = mapOf("peso" to nuevoPeso, "weightHistory" to listaActualizada)
            userRef.set(nuevosDatos, SetOptions.merge()).addOnSuccessListener {
                val formato = SimpleDateFormat("dd MMM", Locale.getDefault())
                val fechaHoy = formato.format(Timestamp.now().toDate())
                snackbarMensaje.value = if (actualizado) {
                    "Actualizamos peso de hoy - $fechaHoy"
                } else {
                    "Peso guardado correctamente - $fechaHoy"
                }
                snackbarActivo.value = true
                loadProgress()
                cargarPesosDesdeFirebase()
                val objetivo = doc.getDouble("pesoObjetivo")?.toFloat()
                val permission = androidx.core.content.ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
                if (objetivo != null && nuevoPeso <= objetivo &&
                    permission == android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    NotificationHelper.mostrarNotificacionObjetivo(context)
                }
            }
        }
    }
}
