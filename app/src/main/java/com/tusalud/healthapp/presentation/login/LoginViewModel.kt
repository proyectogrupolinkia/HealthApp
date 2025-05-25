
package com.tusalud.healthapp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tusalud.healthapp.domain.model.User
import com.tusalud.healthapp.domain.use_case.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

 //inyectoa ala vez todos los caso de uso de login
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

     // Variables del formulario

     var email by mutableStateOf("")
    var password by mutableStateOf("")
    var nombre by mutableStateOf("")
    var edad by mutableStateOf("")
    var peso by mutableStateOf("")
    var altura by mutableStateOf("")
    var user by mutableStateOf<User?>(null)
    var error by mutableStateOf<String?>(null)
    var loading by mutableStateOf(false)

    private val _nombreUsuario = MutableStateFlow("")
    val nombreUsuario: StateFlow<String> = _nombreUsuario

    private val _emailUsuario = MutableStateFlow("")
    val emailUsuario: StateFlow<String> = _emailUsuario
     // Validación del formulario de login

    val isFormValid: Boolean
        get() = isEmailValid(email) && isPasswordValid(password)
     // Validaciones individuales para cada campo de registro

    val edadValida: Boolean
        get() = isEdadValid(edad)

    val pesoValido: Boolean
        get() = isPesoValid(peso)

    val alturaValida: Boolean
        get() = isAlturaValid(altura)
     // Validación global del formulario de registro

    val isRegisterFormValid: Boolean
        get() = nombre.isNotBlank() &&
                isEmailValid(email) &&
                isPasswordValid(password) &&
                edadValida &&
                pesoValido &&
                alturaValida
//      Inicia sesión con email y contraseña.
//
//      Si es correcto, llama a `onSuccess`.


    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            loading = true
            val result = authUseCases.login(email, password)
            loading = false
            result.onSuccess {
                user = it
                onSuccess()
            }.onFailure {
                error = "Usuario o Contraseña incorrectos!"
            }
        }
    }
//Valida si  email
    fun isEmailValid(input: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }
//Valida contraseña

    fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        return passwordRegex.matches(password)
    }

    fun isEdadValid(input: String): Boolean {
        return input.toIntOrNull()?.let { it in 1..120 } ?: false
    }

    fun isPesoValid(input: String): Boolean {
        return input.toFloatOrNull()?.let { it in 1f..500f } ?: false
    }

    fun isAlturaValid(input: String): Boolean {
        return input.toFloatOrNull()?.let { it in 50f..250f } ?: false
    }
//
//       Registra un nuevo usuario en Firebase.
//       Usa la clase `User` con pesoInicial = pesoActual.
//
    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            loading = true
            val newUser = User("", nombre, email, edad.toInt(), peso.toFloat(), peso.toFloat(), altura.toFloat())
            val result = authUseCases.register(newUser, password)
            loading = false
            result.onSuccess {
                user = it
                onSuccess()
            }.onFailure {
                error = it.message
            }
        }
    }
     // Envía un correo para restablecer contraseña.

    fun resetPassword(correo: String, onSent: () -> Unit) {
        viewModelScope.launch {
            loading = true
            val result = authUseCases.resetPassword(correo)
            loading = false
            result.onSuccess {
                error = "Correo enviado para recuperar contraseña"
                onSent()
            }.onFailure {
                error = it.message
            }
        }
    }


}
