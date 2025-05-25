/**
 * Modelo de datos que representa la información básica del perfil de un usuario.
 * Se utiliza para mostrar y editar datos personales en pantallas como Perfil o EditarPerfil.
 */
package com.tusalud.healthapp.domain.model

data class UserProfileData(
    val nombre: String,
    val correo: String,
    val pesoInicio: String,
    val pesoObjetivo: String,
    val edad: String
)
