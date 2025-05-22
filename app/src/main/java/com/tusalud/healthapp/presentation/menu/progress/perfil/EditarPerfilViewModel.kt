package com.tusalud.healthapp.presentation.menu.progress.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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

    private val _edad = MutableStateFlow("")
    val edad: StateFlow<String> = _edad

    private val _pesoInicio = MutableStateFlow("")
    val pesoInicio: StateFlow<String> = _pesoInicio

    private val _pesoObjetivo = MutableStateFlow("")
    val pesoObjetivo: StateFlow<String> = _pesoObjetivo

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        val user = auth.currentUser ?: return
        firestore.collection("users").document(user.uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    _edad.value = doc.getString("edad") ?: ""
                    _pesoInicio.value = doc.getString("pesoInicio") ?: ""
                    _pesoObjetivo.value = doc.getString("pesoObjetivo") ?: ""
                }
            }
    }

    fun onEdadChanged(newEdad: String) {
        _edad.value = newEdad
    }

    fun onPesoInicioChanged(newPeso: String) {
        _pesoInicio.value = newPeso
    }

    fun onPesoObjetivoChanged(newPeso: String) {
        _pesoObjetivo.value = newPeso
    }

    fun isEdadValida(): Boolean {
        return _edad.value.matches(Regex("^\\d{1,3}\$")) &&
                (_edad.value.toIntOrNull() in 1..120)
    }

    fun isPesoValido(peso: String): Boolean {
        val pesoFloat = peso.toFloatOrNull()
        return peso.matches(Regex("^\\d{1,3}(\\.\\d{1,2})?\$")) &&
                pesoFloat != null &&
                pesoFloat in 20.0..500.0
    }

    fun isFormValid(pesoInicio: String, pesoObjetivo: String): Boolean {
        return isEdadValida() &&
                isPesoValido(pesoInicio) &&
                isPesoValido(pesoObjetivo)
    }

    fun updateProfile() {
        val user = auth.currentUser ?: return
        val edadValue = _edad.value
        val pesoIni = _pesoInicio.value
        val pesoObj = _pesoObjetivo.value

        viewModelScope.launch {
            val userProfileData = mapOf(
                "edad" to edadValue,
                "pesoInicio" to pesoIni,
                "pesoObjetivo" to pesoObj
            )

            firestore.collection("users").document(user.uid)
                .update(userProfileData)
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








