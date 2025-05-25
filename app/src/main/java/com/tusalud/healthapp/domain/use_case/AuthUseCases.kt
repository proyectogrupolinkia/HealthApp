//
// Clase contenedora que agrupa todos los casos de uso relacionados con autenticación.

package com.tusalud.healthapp.domain.use_case

import javax.inject.Inject

data class AuthUseCases @Inject constructor(
    val login: LoginUseCase,
    val register: RegisterUseCase,
    val resetPassword: ResetPasswordUseCase,
    val getUserById: GetUserByIdUseCase
)
