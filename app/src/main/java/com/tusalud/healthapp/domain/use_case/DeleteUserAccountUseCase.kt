
//Caso de uso encargado de eliminar la cuenta del usuario autenticado.
package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.repository.UserRepository
import javax.inject.Inject

class DeleteUserAccountUseCase @Inject constructor(
    private val repository: UserRepository
) {

    /**
     * Ejecuta la eliminación de cuenta del usuario actual.
     *
     * @return Result<Unit> indicando si fue exitoso o si ocurrió un error.
     */
    suspend operator fun invoke(): Result<Unit> {
        return repository.deleteUserAccount()
    }
}
