package com.tusalud.healthapp.presentation.menu.progress.peso

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EvolucionPesoViewModel @Inject constructor() : ViewModel() {


    private val _pesosConFechas = MutableStateFlow<List<Pair<Float, String>>>(emptyList())
    val pesosConFechas: StateFlow<List<Pair<Float, String>>> = _pesosConFechas

    init {
        cargarPesos()
    }

    private fun cargarPesos() {
        viewModelScope.launch {

            val datos = listOf(
                70.5f to "01/01",
                71.0f to "08/01",
                70.0f to "15/01",
                69.5f to "22/01"
            )
            _pesosConFechas.value = datos
        }
    }
}
