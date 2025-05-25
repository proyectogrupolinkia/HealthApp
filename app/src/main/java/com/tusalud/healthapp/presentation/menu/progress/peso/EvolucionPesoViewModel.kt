package com.tusalud.healthapp.presentation.menu.progress.peso

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tusalud.healthapp.domain.use_case.GetWeightHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class EvolucionPesoViewModel @Inject constructor(
    private val getWeightHistoryUseCase: GetWeightHistoryUseCase
) : ViewModel() {

    private val _pesosConFechas = MutableStateFlow<List<Pair<Float, String>>>(emptyList())
    val pesosConFechas: StateFlow<List<Pair<Float, String>>> = _pesosConFechas

    private val _pesoObjetivo = MutableStateFlow("")
    val pesoObjetivo: StateFlow<String> = _pesoObjetivo

    fun cargarDatosEvolucion() {
        _pesosConFechas.value = emptyList()

        viewModelScope.launch {
            val result = getWeightHistoryUseCase()
            result.onSuccess { (_, listaPesosConFechas) ->
                _pesosConFechas.value = listaPesosConFechas
            }.onFailure {
                _pesosConFechas.value = emptyList()
            }

            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val document = FirebaseFirestore.getInstance().collection("usuarios").document(uid).get().await()
            _pesoObjetivo.value = document.getDouble("pesoObjetivo")?.toString() ?: ""
        }
    }
}
