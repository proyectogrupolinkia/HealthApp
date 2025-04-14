package com.tusalud.healthapp.domain.respository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tusalud.healthapp.domain.model.UserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.tusalud.healthapp.domain.model.User
class UserRepositoryImpl(auth1: FirebaseAuth, firestore: FirebaseFirestore) : UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
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

    override suspend fun register(user: User, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val result = auth.createUserWithEmailAndPassword(user.correo, password).await()
            val uid = result.user?.uid ?: return@withContext Result.failure(Exception("UID nulo"))
            val userWithId = user.copy(id = uid)

            db.collection("usuarios").document(uid).set(userWithId).await()
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
    override suspend fun getUser(uid: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val doc = db.collection("usuarios").document(uid).get().await()
            val user = doc.toObject(UserDto::class.java)?.toDomain()
                ?: return@withContext Result.failure(Exception("Usuario no encontrado"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}