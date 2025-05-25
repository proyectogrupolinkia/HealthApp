//Caso de uso que recupera un objeto User completo
package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.model.User
import com.tusalud.healthapp.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository
) {


    suspend operator fun invoke(uid: String): Result<User> {
        return repository.getUserById(uid)
    }
}
