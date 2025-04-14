package com.tusalud.healthapp.domain.respository

import com.tusalud.healthapp.domain.model.User
interface UserRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(user: User, password: String): Result<User>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun getUser(uid: String): Result<User>
}