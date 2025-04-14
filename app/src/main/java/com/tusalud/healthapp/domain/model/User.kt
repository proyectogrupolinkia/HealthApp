package com.tusalud.healthapp.domain.model

data class User(
    val id: String,
    val nombre: String,
    val correo: String,
    val edad: Int,
    val peso: Float,
    val altura:Float
)