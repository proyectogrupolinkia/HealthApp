package com.tusalud.healthapp.presentation.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarPerfilViewModel @Inject constructor() : ViewModel() {

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

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    fun cargarDatosUsuario() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val uid = user.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(uid).get().addOnSuccessListener { document ->
            _displayName.value = document.getString("nombre") ?: ""
            _email.value = user.email ?: ""
            _pesoInicio.value = document.getDouble("pesoInicio")?.toString() ?: ""
            _pesoObjetivo.value = document.getDouble("pesoObjetivo")?.toString() ?: ""
            _edad.value = document.getLong("edad")?.toString() ?: ""
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

    fun onEdadChanged(nuevaEdad: String) {
        _edad.value = nuevaEdad
    }

    fun isEdadValida(): Boolean {
        return _edad.value.toIntOrNull()?.let { it in 1..120 } ?: false
    }

    fun isPesoValido(peso: String): Boolean {
        return peso.toFloatOrNull()?.let { it in 1f..500f } ?: false
    }

    fun updateProfile(onSuccess: () -> Unit = {}) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val newName = _displayName.value
        val newEmail = _email.value
        val pesoIni = _pesoInicio.value.toFloatOrNull()
        val pesoObj = _pesoObjetivo.value.toFloatOrNull()

        viewModelScope.launch {
            if (newName != user.displayName) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = newName
                }
                user.updateProfile(profileUpdates)
            }

            if (newEmail != user.email) {
                user.updateEmail(newEmail)
            }

            val datosPerfil = mutableMapOf<String, Any>(
                "nombre" to newName,
                "email" to newEmail
            )
            pesoIni?.let { datosPerfil["pesoInicio"] = it }
            pesoObj?.let { datosPerfil["pesoObjetivo"] = it }
            val edadInt = _edad.value.toIntOrNull()
            edadInt?.let { datosPerfil["edad"] = it }

            FirebaseFirestore.getInstance().collection("usuarios").document(user.uid)
                .set(datosPerfil, SetOptions.merge())
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _toastMessage.emit("Perfil actualizado con éxito")
                        onSuccess()
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _toastMessage.emit("Error al guardar perfil")
                    }
                }
        }
    }
}
