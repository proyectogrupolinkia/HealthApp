package com.tusalud.healthapp.domain.model

data class UserDto(
    val id: String = "",
    val nombre: String = "",
    val correo: String = "",
    val edad: Int = 0,
    val peso: Float = 0f,
    val altura: Float = 0f
) {
    fun toDomain() = User(id, nombre, correo, edad, peso, altura)
}