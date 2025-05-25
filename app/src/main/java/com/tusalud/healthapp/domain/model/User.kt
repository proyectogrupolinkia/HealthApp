package com.tusalud.healthapp.domain.model

data class User(
    val id: String = "",
    val nombre: String = "",
    val correo: String = "",
    val edad: Int = 0,
    val peso: Float = 0f,
    val pesoInicio: Float = 0f,
    val pesoObjetivo: Float = 0f, // ✅ Añade esta línea
    val altura: Int = 0
)
