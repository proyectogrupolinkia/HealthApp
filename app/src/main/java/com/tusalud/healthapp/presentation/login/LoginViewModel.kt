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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var user by mutableStateOf<User?>(null)
    var error by mutableStateOf<String?>(null)
    var loading by mutableStateOf(false)

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            loading = true
            val result = userRepository.login(email, password)
            loading = false
            result.onSuccess {
                user = it
                onSuccess()
            }.onFailure {
                error = it.message
            }
        }
    }
    fun register(nombre: String, correo: String, edad: Int, peso: Float, altura: Float, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            loading = true
            val user = User("", nombre, correo, edad, peso, altura)
            val result = userRepository.register(user, password)
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
}