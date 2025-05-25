package com.tusalud.healthapp.domain.repository

import com.tusalud.healthapp.domain.model.Progress
import com.tusalud.healthapp.domain.model.UpdateWeightResult

interface ProgressRepository {
    suspend fun getProgress(): Result<Progress>
    suspend fun getWeightHistory(): Result<Pair<List<Float>, List<Pair<Float, String>>>>
    suspend fun updateWeight(nuevoPeso: Float): Result<UpdateWeightResult>

}