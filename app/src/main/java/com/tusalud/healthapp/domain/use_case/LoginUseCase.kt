
//Caso de uso para iniciar sesión de un usuario mediante correo y contraseña.

package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.model.User
import com.tusalud.healthapp.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Ejecuta el login del usuario con las credenciales proporcionadas.
 *
 * @param email Correo electrónico del usuario.
 * @param password Contraseña del usuario.
 * @return Result<User> contiene el usuario si fue exitoso, o una excepción si falló.
 */
class LoginUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.login(email, password)
    }
}
