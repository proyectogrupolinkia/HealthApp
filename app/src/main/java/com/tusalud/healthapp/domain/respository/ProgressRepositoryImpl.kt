package com.tusalud.healthapp.data.repository

import com.tusalud.healthapp.domain.model.Progress
import com.tusalud.healthapp.domain.repository.ProgressRepository
import javax.inject.Inject

class ProgressRepositoryImpl @Inject constructor() : ProgressRepository {
    override suspend fun getProgress(): Progress {
        // Simulación de datos de progreso
        return Progress(
            weightKg = 70f,
            heightCm = 175f,
            bodyFatPercentage = 20.5f,
            bmi = 24.5f,
            activeChallenge = "Bajar 6 kg en 3 meses"
        )
    }
}