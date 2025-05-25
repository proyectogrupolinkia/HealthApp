
//Caso de uso para enviar un correo de restablecimiento de contraseña

package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.repository.UserRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: UserRepository
) {
    /**
     * Lanza el proceso de envío del correo de recuperación.
     *
     * @param email Correo del usuario al que se le enviará el enlace.
     * @return Result<Unit> indicando éxito o fallo en la operación.
     */
    suspend operator fun invoke(email: String): Result<Unit> {
        return repository.resetPassword(email)
    }
}
