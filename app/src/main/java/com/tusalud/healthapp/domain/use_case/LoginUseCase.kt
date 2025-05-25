package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.model.User
import com.tusalud.healthapp.domain.respository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.login(email, password)
    }
}
