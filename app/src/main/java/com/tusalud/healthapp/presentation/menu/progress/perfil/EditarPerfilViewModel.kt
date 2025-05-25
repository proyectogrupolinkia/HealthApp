/**
 * ViewModel para la pantalla de edición del perfil.
 * Maneja los campos editables del usuario y su persistencia en Firestore.
 */

package com.tusalud.healthapp.presentation.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tusalud.healthapp.domain.use_case.GetUserProfileUseCase
import com.tusalud.healthapp.domain.use_case.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


//Inyecta 2 casos de uso: GetUserProfileUseCase y UpdateUserProfileUseCase


@HiltViewModel
class EditarPerfilViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

    private val _displayName = MutableStateFlow("")
    val displayName: StateFlow<String> = _displayName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _pesoInicio = MutableStateFlow("")
    val pesoInicio: StateFlow<String> = _pesoInicio

    private val _pesoObjetivo = MutableStateFlow("")
    val pesoObjetivo: StateFlow<String> = _pesoObjetivo

    private val _edad = MutableStateFlow("")
    val edad: StateFlow<String> = _edad

    // Mesnaje  Toast

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    fun onDisplayNameChanged(newName: String) {
        _displayName.value = newName
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPesoInicioChanged(newPeso: String) {
        _pesoInicio.value = newPeso
    }

    fun onPesoObjetivoChanged(newPeso: String) {
        _pesoObjetivo.value = newPeso
    }

    fun onEdadChanged(nuevaEdad: String) {
        _edad.value = nuevaEdad
    }
    // Validación de edad (1 a 120 años)

    fun isEdadValida(): Boolean {
        return _edad.value.toIntOrNull()?.let { it in 1..120 } ?: false
    }
    // Validación de peso (1 a 500 kg)

    fun isPesoValido(peso: String): Boolean {
        return peso.toFloatOrNull()?.let { it in 1f..500f } ?: false
    }

    fun updateProfile(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val result = updateUserProfileUseCase(
                nombre = _displayName.value,
                email = _email.value,
                pesoInicio = _pesoInicio.value,
                pesoObjetivo = _pesoObjetivo.value,
                edad = _edad.value
            )

            result.onSuccess {
                _toastMessage.emit("Perfil actualizado con éxito")
                onSuccess()
            }.onFailure {
                _toastMessage.emit("Error al guardar perfil")
            }
        }
    }
    fun cargarDatosUsuario() {
        viewModelScope.launch {
            val result = getUserProfileUseCase()
            result.onSuccess { perfil ->
                _displayName.value = perfil.nombre
                _email.value = perfil.correo
                _pesoInicio.value = perfil.pesoInicio
                _pesoObjetivo.value = perfil.pesoObjetivo
                _edad.value = perfil.edad
            }.onFailure {
                _toastMessage.emit("Error al cargar los datos del perfil")
            }
        }
    }

}
