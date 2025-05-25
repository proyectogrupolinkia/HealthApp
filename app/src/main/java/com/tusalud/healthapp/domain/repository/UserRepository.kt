//Interfaz del repositorio de usuario que define las operaciones relacionadas con autenticación
// y gestión de perfil. sirve de intermediario con  Firebase

package com.tusalud.healthapp.domain.repository

import com.tusalud.healthapp.domain.model.User
import com.tusalud.healthapp.domain.model.UserProfileData

interface UserRepository {
    /**
     * Inicia sesión con correo y contraseña.
     * @return Result<User> con el usuario autenticado o error.
     */
    suspend fun login(email: String, password: String): Result<User>
    /**
     * Registra un nuevo usuario con sus datos y contraseña.
     * @return Result<User> con el usuario registrado o error.
     */
    suspend fun register(user: User, password: String): Result<User>
    /**
     * Envía un correo para restablecer la contraseña.
     * @return Result<Unit> indicando éxito o fallo.
     */
    suspend fun resetPassword(email: String): Result<Unit>
    /**
     * Obtiene un usuario por su UID desde la base de datos.
     * @return Result<User> con los datos del usuario o error.
     */
    suspend fun getUserById(uid: String): Result<User>

    /**
     * Recupera los datos de perfil del usuario actual (nombre, correo, edad, pesos).
     * @return Result<UserProfileData>
     */
    suspend fun getUserProfile(): Result<UserProfileData>
    /**
     * Elimina completamente la cuenta del usuario autenticado.
     * @return Result<Unit>
     */
    suspend fun deleteUserAccount(): Result<Unit>
    /**
     * Cierra la sesión del usuario.
     */
    fun logout()
    /**
     * Actualiza los datos de perfil del usuario.
     * @return Result<Unit> indicando éxito o error.
     */
    suspend fun updateUserProfile(
        nombre: String,
        email: String,
        pesoInicio: String,
        pesoObjetivo: String,
        edad: String
    ): Result<Unit>

}
