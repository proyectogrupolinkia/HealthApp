package com.tusalud.healthapp.presentation.menu.progress.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarPerfilViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _displayName = MutableStateFlow(auth.currentUser?.displayName ?: "")
    val displayName: StateFlow<String> = _displayName

    private val _email = MutableStateFlow(auth.currentUser?.email ?: "")
    val email: StateFlow<String> = _email

    private val _pesoInicio = MutableStateFlow("")
    val pesoInicio: StateFlow<String> = _pesoInicio

    private val _pesoObjetivo = MutableStateFlow("")
    val pesoObjetivo: StateFlow<String> = _pesoObjetivo

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    init {
        cargarPesos()
    }

    private fun cargarPesos() {
        val user = auth.currentUser ?: return
        firestore.collection("users").document(user.uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    _pesoInicio.value = doc.getString("pesoInicio") ?: ""
                    _pesoObjetivo.value = doc.getString("pesoObjetivo") ?: ""
                }
            }
    }

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

    fun updateProfile() {
        val user = auth.currentUser
        val newName = _displayName.value
        val newEmail = _email.value
        val pesoIni = _pesoInicio.value
        val pesoObj = _pesoObjetivo.value

        viewModelScope.launch {
            if (user == null) {
                emitToast("Usuario no encontrado")
                return@launch
            }

            if (newName != user.displayName) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = newName
                }
                user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                    viewModelScope.launch {
                        if (task.isSuccessful) {
                            emitToast("Nombre de usuario actualizado correctamente")
                        } else {
                            emitToast("Error al actualizar nombre de usuario")
                        }
                    }
                }
            }

            if (newEmail != user.email) {
                user.updateEmail(newEmail).addOnCompleteListener { task ->
                    viewModelScope.launch {
                        if (task.isSuccessful) {
                            emitToast("Correo electrónico actualizado correctamente")
                        } else {
                            emitToast("Error al actualizar correo electrónico")
                        }
                    }
                }
            }

            val userProfileData = mapOf(
                "displayName" to newName,
                "email" to newEmail,
                "pesoInicio" to pesoIni,
                "pesoObjetivo" to pesoObj
            )

            firestore.collection("users").document(user.uid)
                .set(userProfileData)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        emitToast("Perfil actualizado con éxito")
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        emitToast("Error al guardar perfil")
                    }
                }
        }
    }

    private suspend fun emitToast(message: String) {
        _toastMessage.emit(message)
    }
}





