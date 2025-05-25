package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.model.UpdateWeightResult
import com.tusalud.healthapp.domain.repository.ProgressRepository
import javax.inject.Inject

class UpdateWeightUseCase @Inject constructor(
    private val repository: ProgressRepository
) {
    suspend operator fun invoke(nuevoPeso: Float): Result<UpdateWeightResult> {
        return repository.updateWeight(nuevoPeso)
    }
}
