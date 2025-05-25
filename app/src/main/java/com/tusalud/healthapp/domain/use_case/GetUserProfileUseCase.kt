
//Caso de uso encargado de obtener los datos del perfil del usuario.

package com.tusalud.healthapp.domain.use_case


import com.tusalud.healthapp.domain.model.UserProfileData
import com.tusalud.healthapp.domain.repository.UserRepository

class GetUserProfileUseCase(
    private val repository: UserRepository
) {
    /**
    * Ejecuta la recuperación del perfil del usuario actual.
    *
    * @return Result<UserProfileData> que puede contener:
    *   - nombre
    *   - correo
    *   - pesoInicial
    *   - pesoObjetivo
    *   - edad
    *
    * Devuelve un error si no hay usuario autenticado o si falla el acceso a la base de datos.
    */
    suspend operator fun invoke(): Result<UserProfileData> {
        return repository.getUserProfile()
    }
}