///
//  ViewModel para la pantalla de configuración.
//  Permite al usuario gestionar opciones como notificaciones,
//  restablecer la contraseña y cerrar sesión.
// /

package com.tusalud.healthapp.presentation.menu.progress.configuracion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de configuración.
 * Permite al usuario gestionar opciones como notificaciones,
 * restablecer la contraseña y cerrar sesión.
 */
class ConfiguracionViewModel : ViewModel() {

    // Instancia de FirebaseAuth para acceder al usuario actual
    private val auth = FirebaseAuth.getInstance()

    // Estado para activar o desactivar notificaciones
    private val _notificacionesActivadas = MutableStateFlow(true)
    val notificacionesActivadas: StateFlow<Boolean> = _notificacionesActivadas

    // Flujo para enviar mensajes Toast a la UI
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    /**
     * Activa o desactiva las notificaciones.
     * Este valor se refleja en la interfaz y podría persistirse en un futuro.
     */
    fun toggleNotificaciones(enabled: Boolean) {
        _notificacionesActivadas.value = enabled
    }

    /**
     * Envía un correo de restablecimiento de contraseña
     */
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

    /**
     * Cierra la sesión del usuario actual.
     * Ejecuta la función `onLogout()`
     */
    fun logout(onLogout: () -> Unit) {
        auth.signOut()
        onLogout()
    }
}
