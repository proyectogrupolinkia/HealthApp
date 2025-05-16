package com.tusalud.healthapp.domain.model

data class Progress(
    val peso: Float = 0f,
    val heightCm: Float = 0f,
    val bmi: Float = 0f,
    val pesoObjetivo: Float? = null
)