package com.tusalud.healthapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tusalud.healthapp.domain.model.Progress
import com.tusalud.healthapp.domain.repository.ProgressRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProgressRepositoryImpl @Inject constructor() : ProgressRepository {

    override suspend fun getProgress(): Progress {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return Progress()
        val db = FirebaseFirestore.getInstance()
        val snapshot = db.collection("usuarios").document(userId).get().await()

        val peso = snapshot.getDouble("peso")?.toFloat() ?: 0f
        val height = snapshot.getDouble("altura")?.toFloat() ?: 0f
        val pesoObjetivo = snapshot.getDouble("pesoObjetivo")?.toFloat()
        val altura = snapshot.getDouble("altura")?.toFloat() ?: 0f
        val bmi = if (height > 0) peso / ((height / 100) * (height / 100)) else 0f

        return Progress(
            peso = peso,
            heightCm = height,
            bmi = bmi,
            pesoObjetivo = snapshot.getDouble("pesoObjetivo")?.toFloat()
        )
    }
}