//Caso de uso encargado de cerrar la sesión del usuario.

package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke() {
        repository.logout()
    }
}
