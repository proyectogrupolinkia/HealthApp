/**
 * Data Transfer Object (DTO) que representa un usuario tal como se guarda
 * o recupera desde Firebase Firestore.
 *
 * Esta clase es utilizada exclusivamente en la capa de datos.
 */

package com.tusalud.healthapp.data.model

import com.tusalud.healthapp.domain.model.User

data class UserDto(
    val id: String = "",
    val nombre: String = "",
    val correo: String = "",
    val edad: Int = 0,
    val peso: Float = 0f,
    val pesoInicio: Float = 0f,
    val pesoObjetivo: Float = 0f, //
    val altura: Int = 0
) {

    /**
     * Convierte un UserDto a un objeto del modelo de dominio User.
     */
    fun toDomain(): User = User(
        id, nombre, correo, edad, peso, pesoInicio, pesoObjetivo, altura
    )
}