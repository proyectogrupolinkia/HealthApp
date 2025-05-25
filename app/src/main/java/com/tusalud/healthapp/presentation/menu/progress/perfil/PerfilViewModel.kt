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

    private val _displayName = MutableStateFlow("Usuario")
    val displayName: StateFlow<String> = _displayName

    private val _email = MutableStateFlow("sin correo")
    val email: StateFlow<String> = _email

    private val _pesoInicio = MutableStateFlow("")
    private val _pesoObjetivo = MutableStateFlow("")

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog: StateFlow<Boolean> = _showLogoutDialog

    private val _showHelpDialog = MutableStateFlow(false)
    val showHelpDialog: StateFlow<Boolean> = _showHelpDialog

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog

    init {
        cargarDatosUsuario()
    }

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

    fun eliminarCuenta(onComplete: () -> Unit) {
        viewModelScope.launch {
            val result = deleteUserAccountUseCase()
            result.onSuccess { onComplete() }
            // onFailure opcional: mostrar error al usuario si se desea
        }
    }
}
