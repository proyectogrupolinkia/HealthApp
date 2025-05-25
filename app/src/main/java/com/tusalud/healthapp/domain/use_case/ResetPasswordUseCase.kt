package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.respository.UserRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return repository.resetPassword(email)
    }
}
