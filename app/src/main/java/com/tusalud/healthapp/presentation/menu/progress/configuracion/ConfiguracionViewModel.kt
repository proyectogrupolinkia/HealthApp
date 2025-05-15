package com.tusalud.healthapp.presentation.menu.progress.configuracion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ConfiguracionViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _notificacionesActivadas = MutableStateFlow(true)
    val notificacionesActivadas: StateFlow<Boolean> = _notificacionesActivadas

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    fun toggleNotificaciones(enabled: Boolean) {
        _notificacionesActivadas.value = enabled
    }

    fun sendPasswordResetEmail() {
        val email = auth.currentUser?.email
        viewModelScope.launch {
            if (!email.isNullOrBlank()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        viewModelScope.launch {
                            if (task.isSuccessful) {
                                _toastMessage.emit("Se ha enviado un correo para restablecer la contraseña")
                            } else {
                                _toastMessage.emit("Error al enviar correo de restablecimiento")
                            }
                        }
                    }
            } else {
                _toastMessage.emit("No se encontró un correo asociado")
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        auth.signOut()
        onLogout()
    }
}
