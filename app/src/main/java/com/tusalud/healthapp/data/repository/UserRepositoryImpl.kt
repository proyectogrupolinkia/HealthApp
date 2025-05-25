package com.tusalud.healthapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tusalud.healthapp.data.model.UserDto
import com.tusalud.healthapp.domain.model.User
import com.tusalud.healthapp.domain.model.UserProfileData
import com.tusalud.healthapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : UserRepository {

    override suspend fun login(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: return@withContext Result.failure(Exception("UID nulo"))
                val doc = db.collection("usuarios").document(uid).get().await()
                val user = doc.toObject(UserDto::class.java)?.toDomain()
                    ?: return@withContext Result.failure(Exception("Usuario no encontrado"))

                Result.success(user)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun register(user: User, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            try {
                val result = auth.createUserWithEmailAndPassword(user.correo, password).await()
                val uid = result.user?.uid ?: return@withContext Result.failure(Exception("UID nulo"))
                val userWithId = user.copy(id = uid)

                db.collection("usuarios").document(uid).set(
                    hashMapOf(
                        "id" to userWithId.id,
                        "nombre" to userWithId.nombre,
                        "correo" to userWithId.correo,
                        "edad" to userWithId.edad,
                        "peso" to userWithId.peso,
                        "pesoInicio" to userWithId.pesoInicio,
                        "altura" to userWithId.altura,
                        "pesoObjetivo" to userWithId.pesoObjetivo
                    )
                ).await()

                Result.success(userWithId)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun resetPassword(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(uid: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val doc = db.collection("usuarios").document(uid).get().await()
            val user = doc.toObject(UserDto::class.java)?.toDomain()
                ?: return@withContext Result.failure(Exception("Usuario no encontrado"))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(): Result<UserProfileData> = withContext(Dispatchers.IO) {
        val user = auth.currentUser ?: return@withContext Result.failure(Exception("Usuario no autenticado"))
        try {
            val doc = db.collection("usuarios").document(user.uid).get().await()
            val profile = UserProfileData(
                nombre = doc.getString("nombre") ?: "",
                correo = user.email ?: "",
                pesoInicio = doc.getDouble("pesoInicio")?.toString() ?: "",
                pesoObjetivo = doc.getDouble("pesoObjetivo")?.toString() ?: "",
                edad = doc.getLong("edad")?.toString() ?: ""
            )
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun deleteUserAccount(): Result<Unit> = withContext(Dispatchers.IO) {
        val user = auth.currentUser ?: return@withContext Result.failure(Exception("Usuario no autenticado"))

        try {
            db.collection("usuarios")
                .document(user.uid)
                .delete()
                .await()

            user.delete().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override fun logout() {
        auth.signOut()
    }
    override suspend fun updateUserProfile(
        nombre: String,
        email: String,
        pesoInicio: String,
        pesoObjetivo: String,
        edad: String
    ): Result<Unit> {
        val user = auth.currentUser ?: return Result.failure(Exception("Usuario no autenticado"))

        return try {
            if (nombre != user.displayName) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = nombre
                }
                user.updateProfile(profileUpdates).await()
            }

            if (email != user.email) {
                user.updateEmail(email).await()
            }

            val datosPerfil = mutableMapOf<String, Any>(
                "nombre" to nombre,
                "email" to email
            )

            pesoInicio.toFloatOrNull()?.let { datosPerfil["pesoInicio"] = it }
            pesoObjetivo.toFloatOrNull()?.let { datosPerfil["pesoObjetivo"] = it }
            edad.toIntOrNull()?.let { datosPerfil["edad"] = it }

            db.collection("usuarios")
                .document(user.uid)
                .set(datosPerfil, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
