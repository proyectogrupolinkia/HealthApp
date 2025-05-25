package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.model.User
import com.tusalud.healthapp.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User, password: String): Result<User> {
        return repository.register(user, password)
    }
}
