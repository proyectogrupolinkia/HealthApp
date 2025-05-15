package com.tusalud.healthapp.presentation.menu.progress.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PerfilViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _displayName = MutableStateFlow("Usuario")
    val displayName: StateFlow<String> = _displayName

    private val _email = MutableStateFlow("sin correo")
    val email: StateFlow<String> = _email

    private val _pesoInicio = MutableStateFlow("")
    val pesoInicio: StateFlow<String> = _pesoInicio

    private val _pesoObjetivo = MutableStateFlow("")
    val pesoObjetivo: StateFlow<String> = _pesoObjetivo

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog: StateFlow<Boolean> = _showLogoutDialog

    private val _showHelpDialog = MutableStateFlow(false)
    val showHelpDialog: StateFlow<Boolean> = _showHelpDialog

    init {
        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        val user = auth.currentUser ?: return

        viewModelScope.launch {
            _displayName.value = user.displayName ?: "Usuario"
            _email.value = user.email ?: "sin correo"
        }

        //cargar pesos desde Firestore
        firestore.collection("users").document(user.uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    _pesoInicio.value = doc.getString("pesoInicio") ?: ""
                    _pesoObjetivo.value = doc.getString("pesoObjetivo") ?: ""
                }
            }
    }

    fun setShowLogoutDialog(show: Boolean) {
        _showLogoutDialog.value = show
    }

    fun setShowHelpDialog(show: Boolean) {
        _showHelpDialog.value = show
    }

    fun logout(onLogoutComplete: () -> Unit) {
        auth.signOut()
        onLogoutComplete()
    }
}

