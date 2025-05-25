package com.tusalud.healthapp.domain.repository

import com.tusalud.healthapp.domain.model.User
import com.tusalud.healthapp.domain.model.UserProfileData

interface UserRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(user: User, password: String): Result<User>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun getUserById(uid: String): Result<User>           // <- ESTA línea debe estar
    suspend fun getUserProfile(): Result<UserProfileData>
    suspend fun deleteUserAccount(): Result<Unit>
    fun logout()
    suspend fun updateUserProfile(
        nombre: String,
        email: String,
        pesoInicio: String,
        pesoObjetivo: String,
        edad: String
    ): Result<Unit>

}
