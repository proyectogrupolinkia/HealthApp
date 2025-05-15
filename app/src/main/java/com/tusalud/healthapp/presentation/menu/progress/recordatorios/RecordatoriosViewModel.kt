package com.tusalud.healthapp.presentation.menu.progress.recordatorios

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecordatoriosViewModel : ViewModel() {

    private val _recordatorios = MutableStateFlow<List<String>>(emptyList())
    val recordatorios: StateFlow<List<String>> = _recordatorios

    private val _nuevoRecordatorio = MutableStateFlow("")
    val nuevoRecordatorio: StateFlow<String> = _nuevoRecordatorio

    fun onNuevoRecordatorioChanged(nuevo: String) {
        _nuevoRecordatorio.value = nuevo
    }

    fun agregarRecordatorio() {
        val texto = _nuevoRecordatorio.value.trim()
        if (texto.isNotEmpty()) {
            _recordatorios.value = _recordatorios.value + texto
            _nuevoRecordatorio.value = ""
        }
    }

    fun eliminarRecordatorio(recordatorio: String) {
        _recordatorios.value = _recordatorios.value - recordatorio
    }
}
