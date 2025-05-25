/**
 * ViewModel que gestiona la lógica de recordatorios personalizados del usuario.
 * Utiliza StateFlow para representar el estado reactivo de la lista y del texto nuevo.
 */

package com.tusalud.healthapp.presentation.menu.progress.recordatorios

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



class RecordatoriosViewModel : ViewModel() {

    // Lista de recordatorios actuales (inmutable desde la vista)

    private val _recordatorios = MutableStateFlow<List<String>>(emptyList())
    val recordatorios: StateFlow<List<String>> = _recordatorios

    // Texto del nuevo recordatorio en edición

    private val _nuevoRecordatorio = MutableStateFlow("")
    val nuevoRecordatorio: StateFlow<String> = _nuevoRecordatorio

    /**
     * Actualiza el valor del texto del nuevo recordatorio conforme el usuario escribe.
     */

    fun onNuevoRecordatorioChanged(nuevo: String) {
        _nuevoRecordatorio.value = nuevo
    }
    /**
     * Agrega un nuevo recordatorio a la lista si no está vacío.
     * También limpia el campo de entrada después de agregar.
     */
    fun agregarRecordatorio() {
        val texto = _nuevoRecordatorio.value.trim()
        if (texto.isNotEmpty()) {
            _recordatorios.value = _recordatorios.value + texto
            _nuevoRecordatorio.value = ""
        }
    }
    /**
     * Elimina un recordatorio específico de la lista.
     */
    fun eliminarRecordatorio(recordatorio: String) {
        _recordatorios.value = _recordatorios.value - recordatorio
    }
}
