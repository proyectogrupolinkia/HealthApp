package com.tusalud.healthapp.data.model

import com.tusalud.healthapp.domain.model.User

data class UserDto(
    val id: String = "",
    val nombre: String = "",
    val correo: String = "",
    val edad: Int = 0,
    val peso: Float = 0f,
    val pesoInicio: Float = 0f,
    val pesoObjetivo: Float = 0f, // ✅ Asegúrate de tenerlo aquí también
    val altura: Int = 0
) {
    fun toDomain(): User = User(
        id, nombre, correo, edad, peso, pesoInicio, pesoObjetivo, altura
    )
}