package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.model.User
import com.tusalud.healthapp.domain.respository.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(uid: String): Result<User> {
        return repository.getUserById(uid)
    }
}
