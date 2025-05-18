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
        val user = auth.currentUser ?: return

        viewModelScope.launch {
            _email.value = user.email ?: "sin correo"
        }

        // Obtener nombre y pesos desde Firestore (colección "usuarios")
        firestore.collection("usuarios").document(user.uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    _displayName.value = doc.getString("nombre") ?: "Usuario"
                    _pesoInicio.value = doc.get("pesoInicio")?.toString() ?: ""
                    _pesoObjetivo.value = doc.get("pesoObjetivo")?.toString() ?: ""
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
        auth.signOut()
        onLogoutComplete()
    }
    fun eliminarCuenta(onComplete: () -> Unit) {
        val user = auth.currentUser ?: return
        val uid = user.uid

        firestore.collection("usuarios").document(uid).delete()
        user.delete()
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { /* Manejo de error opcional */ }
    }
}

