/**
 * ViewModel para la pantalla de perfil del usuario.
 * Maneja los datos del usuario, el cierre de sesión y la eliminación de cuenta.
 */

package com.tusalud.healthapp.presentation.menu.progress.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tusalud.healthapp.domain.use_case.DeleteUserAccountUseCase
import com.tusalud.healthapp.domain.use_case.GetUserProfileUseCase
import com.tusalud.healthapp.domain.use_case.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteUserAccountUseCase: DeleteUserAccountUseCase
) : ViewModel() {
    // Nombre del usuario

    private val _displayName = MutableStateFlow("Usuario")
    val displayName: StateFlow<String> = _displayName
    // Correo electrónico del usuario

    private val _email = MutableStateFlow("sin correo")
    val email: StateFlow<String> = _email
    // Peso inicial y objetivo (no expuestos directamente)

    private val _pesoInicio = MutableStateFlow("")
    private val _pesoObjetivo = MutableStateFlow("")

    // Control de diálogos
    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog: StateFlow<Boolean> = _showLogoutDialog

    private val _showHelpDialog = MutableStateFlow(false)
    val showHelpDialog: StateFlow<Boolean> = _showHelpDialog

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog
 //Al inciar  se cargan los datos del perfil
    init {
        cargarDatosUsuario()
    }
    /**
     * Carga los datos del usuario llamando al caso de uso getUserProfileUseCase.
     */
    internal fun cargarDatosUsuario() {
        viewModelScope.launch {
            val result = getUserProfileUseCase()
            result.onSuccess { data ->
                _email.value = data.correo
                _displayName.value = data.nombre
                _pesoInicio.value = data.pesoInicio
                _pesoObjetivo.value = data.pesoObjetivo
            }
        }
    }

    fun setShowLogoutDialog(show: Boolean) {
        _showLogoutDialog.value = show
    }

    fun setShowHelpDialog(show: Boolean) {
        _showHelpDialog.value = show
    }

    fun setShowDeleteDialog(show: Boolean) {
        _showDeleteDialog.value = show
    }

    fun logout(onLogoutComplete: () -> Unit) {
        logoutUseCase()
        onLogoutComplete()
    }
    /**
     * Llama al caso de uso deleteUserAccountUseCasex para eliminar la cuenta del usuario.
     * Ejecuta una acción de cierre al finalizar.
     */
    fun eliminarCuenta(onComplete: () -> Unit) {
        viewModelScope.launch {
            val result = deleteUserAccountUseCase()
            result.onSuccess { onComplete() }
            // onFailure opcional: mostrar error al usuario si se desea
        }
    }
}
