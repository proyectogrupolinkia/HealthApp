package com.tusalud.healthapp.domain.model
/**
 * Modelo que representa a un usuario en el dominio de la aplicación.
 * Contiene toda la información necesaria para el registro, inicio de sesión
 * y seguimiento del progreso personal.
 */
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
