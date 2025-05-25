package com.tusalud.healthapp.presentation.menu.meditacion

data class EjercicioMeditacion(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val duracionMinutos: Int,
    val imagenUrl: String? = null // Opcional para mostrar un ícono
)