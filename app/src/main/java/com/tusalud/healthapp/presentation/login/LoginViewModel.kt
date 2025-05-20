package com.tusalud.healthapp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tusalud.healthapp.domain.model.User
import com.tusalud.healthapp.domain.respository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var user by mutableStateOf<User?>(null)
    var error by mutableStateOf<String?>(null)
    var loading by mutableStateOf(false)

    private val _nombreUsuario = MutableStateFlow("")
    val nombreUsuario: StateFlow<String> = _nombreUsuario

    private val _emailUsuario = MutableStateFlow("")
    val emailUsuario: StateFlow<String> = _emailUsuario

    val isFormValid: Boolean
        get() = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 6

    fun isEdadValid(input: String): Boolean {
        return input.toIntOrNull()?.let { it in 1..120 } ?: false
    }

    fun isPesoValid(input: String): Boolean {
        return input.toFloatOrNull()?.let { it in 1f..500f } ?: false
    }

    fun isAlturaValid(input: String): Boolean {
        return input.toFloatOrNull()?.let { it in 1f..250f } ?: false
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            loading = true
            val result = userRepository.login(email, password)
            loading = false
            result.onSuccess {
                user = it
                onSuccess()
            }.onFailure {
                error = "Usuario o Contraseña incorrectos!"
            }
        }
    }

    fun isEmailValid(input: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    fun isPasswordValid(input: String): Boolean {
        return input.length >= 6
    }

    fun isRegisterFormValid(
        nombre: String,
        correo: String,
        edad: String,
        peso: String,
        altura: String,
        password: String
    ): Boolean {
        val edadInt = edad.toIntOrNull()
        val pesoFloat = peso.toFloatOrNull()
        val alturaFloat = altura.toFloatOrNull()

        return nombre.isNotBlank() &&
                isEmailValid(correo) &&
                isPasswordValid(password) &&
                edadInt != null && edadInt in 1..120 &&
                pesoFloat != null && pesoFloat in 1f..500f &&
                alturaFloat != null && alturaFloat in 1f..250f
    }



    fun register(
        nombre: String,
        correo: String,
        edad: Int,
        peso: Float,
        altura: Float,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            loading = true
            val newUser = User("", nombre, correo, edad, peso, peso, altura)
            val result = userRepository.register(newUser, password)
            loading = false
            result.onSuccess {
                this@LoginViewModel.user = it
                onSuccess()
            }.onFailure {
                error = it.message
            }
        }
    }

    fun resetPassword(correo: String, onSent: () -> Unit) {
        viewModelScope.launch {
            loading = true
            val result = userRepository.resetPassword(correo)
            loading = false
            result.onSuccess {
                error = "Correo enviado para recuperar contraseña"
                onSent()
            }.onFailure {
                error = it.message
            }
        }
    }

    fun cargarNombreUsuario(uid: String) {
        viewModelScope.launch {
            val result = userRepository.getUserById(uid)
            result.onSuccess { user ->
                _nombreUsuario.value = user.nombre
                _emailUsuario.value = user.correo
            }.onFailure {
                // Manejo de error opcional
            }
        }
    }
}
