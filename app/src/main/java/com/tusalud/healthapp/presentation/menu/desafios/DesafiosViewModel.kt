
// ViewModel que gestiona la lógica de los desafíos activos.
// Maneja el progreso, reinicios diarios y recordatorios personalizados.

package com.tusalud.healthapp.presentation.menu.desafios

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tusalud.healthapp.utils.DesafiosPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Desafio(
    val id: Int,
    val nombre: String,
    val objetivo: Float,
    val progreso: Float,
    val unidad: String,
    val recordatorio: String? = null
)

class DesafiosViewModel : ViewModel() {

    private val _desafios = MutableStateFlow(
        listOf(
            Desafio(
                id = 2,
                nombre = "Bajar 6 kg en 3 meses",
                objetivo = 6f,
                progreso = 1.5f,
                unidad = "kg"
            ),
            Desafio(
                id = 3,
                nombre = "Beber 2 litros agua al día",
                objetivo = 10f,
                progreso = 9f,
                unidad = "veces"
            ),
            Desafio(
                id = 4,
                nombre = "Poner las piernas en alto 10 min",
                objetivo = 7f,
                progreso = 0f,
                unidad = "veces"
            )
        )
    )

    val desafios: StateFlow<List<Desafio>> = _desafios

    /**
     * Marca un desafío como realizado, incrementando el progreso en 1.
     * Solo se actualiza si aún no se alcanzó el objetivo.
     */

    fun marcarDesafioComoRealizado(id: Int) {
        _desafios.update { lista ->
            lista.map { desafio ->
                if (desafio.id == id && desafio.progreso < desafio.objetivo) {
                    desafio.copy(progreso = desafio.progreso + 1)
                } else desafio
            }
        }
    }
    /**
     * Reinicia el progreso de los desafíos diarios (ej. agua, piernas).
     * Solo se reinicia si no se ha hecho aún en el día actual.
     */
    fun resetearDesafiosDiarios(context: Context) {
        viewModelScope.launch {
            if (DesafiosPreferences.shouldResetDesafios(context)) {
                _desafios.update { lista ->
                    lista.map { desafio ->
                        if (desafio.nombre.contains("agua", ignoreCase = true) ||
                            desafio.nombre.contains("piernas", ignoreCase = true)
                        ) {
                            desafio.copy(progreso = 0f)
                        } else desafio
                    }
                }
                DesafiosPreferences.updateResetDate(context)
            }
        }
    }


}


