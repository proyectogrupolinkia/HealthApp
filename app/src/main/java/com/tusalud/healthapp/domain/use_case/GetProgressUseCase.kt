package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.model.Progress
import com.tusalud.healthapp.domain.repository.ProgressRepository
import javax.inject.Inject

class GetProgressUseCase @Inject constructor(
    private val repository: ProgressRepository
) {
    suspend operator fun invoke(): Result<Progress> {
        return repository.getProgress()
    }
}
