/**
 * ViewModel para manejar el estado del progreso del usuario.
 * Se encarga de cargar, actualizar y observar datos como el peso y la evolución del mismo.
 */

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

    // Estado del progreso general (peso actual,  peso objetivo, etc.)

    private val _progress = MutableStateFlow<Progress?>(null)
    val progress: StateFlow<Progress?> = _progress

    // Lista de pesos registrados (sin fechas)


    private val _pesos = MutableStateFlow<List<Float>>(emptyList())
    val pesos: StateFlow<List<Float>> = _pesos

    // Lista de arrays peso + fecha (para graficar la evolución)
    private val _pesosConFechas = MutableStateFlow<List<Pair<Float, String>>>(emptyList())
    val pesosConFechas: StateFlow<List<Pair<Float, String>>> = _pesosConFechas

    var snackbarActivo = MutableStateFlow(false)
    var snackbarMensaje = MutableStateFlow("")

    //  se ejecuta automáticamente cuando se crea una instancia del ProgressViewModel
    init {
        loadProgress()
        cargarPesosDesdeFirebase()
    }

    /**
     * Llama al caso de uso para obtener el progreso actual del usuario.
     */
    fun loadProgress() {
        viewModelScope.launch {
            val result = getProgressUseCase()
            result.onSuccess {
                _progress.value = it
            }
        }
    }
    /**
     * Llama al caso de uso para cargar el historial de peso desde Firestore.
     */

    fun cargarPesosDesdeFirebase() {
        viewModelScope.launch {
            val result = getWeightHistoryUseCase()
            result.onSuccess { (listaPesos, listaPesosConFechas) ->
                _pesos.value = listaPesos
                _pesosConFechas.value = listaPesosConFechas
            }
        }
    }



}
