package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        nombre: String,
        email: String,
        pesoInicio: String,
        pesoObjetivo: String,
        edad: String
    ): Result<Unit> {
        return repository.updateUserProfile(nombre, email, pesoInicio, pesoObjetivo, edad)
    }
}
