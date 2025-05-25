/**
 * Caso de uso encargado de actualizar el perfil del usuario.
 * Este caso de uso aísla la lógica de negocio de la capa de presentación,
 * delegando la persistencia al repositorio correspondiente.
 */

package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {

    /**
     * Ejecuta la actualización de los datos del usuario.
     *
     * @param nombre Nombre del usuario
     * @param email Correo electrónico
     * @param pesoInicio Peso inicial registrado
     * @param pesoObjetivo Meta de peso del usuario
     * @param edad Edad del usuario
     *
     * @return Result<Unit> indicando éxito o error
     */
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
