package com.tusalud.healthapp.domain.use_case

import javax.inject.Inject

data class AuthUseCases @Inject constructor(
    val login: LoginUseCase,
    val register: RegisterUseCase,
    val resetPassword: ResetPasswordUseCase,
    val getUserById: GetUserByIdUseCase
)
