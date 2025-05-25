//Caso de uso que encapsula la lógica para registrar un nuevo usuario.

package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.model.User
import com.tusalud.healthapp.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: UserRepository
) {

    /**
     * Ejecuta el registro con los datos del usuario y la contraseña.
     *
     * @param user Objeto que contiene nombre, correo, edad, peso, altura, etc.
     * @param password Contraseña con la que se registrará el usuario
     *
     * @return Result<User> que contiene el usuario registrado o una excepción si falla
     */
    suspend operator fun invoke(user: User, password: String): Result<User> {
        return repository.register(user, password)
    }
}
