package com.tusalud.healthapp.presentation.menu.progress.peso

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tusalud.healthapp.domain.use_case.UpdateWeightUseCase
import com.tusalud.healthapp.utils.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActualizarPesoViewModel @Inject constructor(
    private val updateWeightUseCase: UpdateWeightUseCase
) : ViewModel() {

    private val _snackbarActivo = MutableStateFlow(false)
    val snackbarActivo: StateFlow<Boolean> = _snackbarActivo

    private val _snackbarMensaje = MutableStateFlow("")
    val snackbarMensaje: StateFlow<String> = _snackbarMensaje

    fun resetSnackbar() {
        _snackbarActivo.value = false
    }

    fun actualizarPeso(context: Context, nuevoPeso: Float?) {
        if (nuevoPeso == null) return

        viewModelScope.launch {
            val result = updateWeightUseCase(nuevoPeso)
            result.onSuccess { resultado ->
                _snackbarMensaje.value = resultado.mensajeSnackbar
                _snackbarActivo.value = true

                val tienePermiso = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                        ActivityCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED

                if (resultado.notificar && tienePermiso) {
                    NotificationHelper.mostrarNotificacionObjetivo(context)
                }
            }.onFailure {
                _snackbarMensaje.value = "Error al actualizar el peso: ${it.message}"
                _snackbarActivo.value = true
            }
        }
    }
    fun validarPeso(input: String): Boolean {
        val regex = Regex("^\\d{1,3}(\\.\\d{0,2})?$")
        if (!regex.matches(input)) return false
        val valor = input.toFloatOrNull() ?: return false
        return valor in 20f..500f
    }

}
