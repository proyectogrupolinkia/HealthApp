package com.tusalud.healthapp.domain.model

data class Progress(
    val weightKg: Float,
    val heightCm: Float,
    val bodyFatPercentage: Float,
    val bmi: Float,
    val activeChallenge: String
)