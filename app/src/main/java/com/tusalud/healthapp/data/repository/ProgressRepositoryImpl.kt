package com.tusalud.healthapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tusalud.healthapp.domain.model.Progress
import com.tusalud.healthapp.domain.repository.ProgressRepository
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import com.google.firebase.Timestamp
import com.google.firebase.firestore.SetOptions
import com.tusalud.healthapp.domain.model.UpdateWeightResult

import java.util.Calendar


class ProgressRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ProgressRepository {

    override suspend fun getProgress(): Result<Progress> {
        val user = auth.currentUser ?: return Result.failure(Exception("Usuario no autenticado"))
        val uid = user.uid

        return try {
            val document = firestore.collection("usuarios").document(uid).get().await()
            val peso = document.getDouble("peso")?.toFloat() ?: 0f
            val altura = document.getDouble("altura")?.toFloat() ?: 0f
            val bmi = if (altura > 0) peso / ((altura / 100) * (altura / 100)) else 0f
            val pesoObjetivo = document.getDouble("pesoObjetivo")?.toFloat()

            Result.success(Progress(peso, altura, bmi, pesoObjetivo))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun getWeightHistory(): Result<Pair<List<Float>, List<Pair<Float, String>>>> {
        val user = auth.currentUser ?: return Result.failure(Exception("Usuario no autenticado"))

        return try {
            val document = firestore.collection("usuarios").document(user.uid).get().await()
            val listaRaw = document.get("weightHistory") as? List<*>
            val lista = listaRaw?.filterIsInstance<Map<String, Any>>() ?: emptyList()

            val pesos = lista.mapNotNull { item ->
                (item["peso"] as? Number)?.toFloat()
            }

            val formato = SimpleDateFormat("dd MMM", Locale.getDefault())
            val pesosConFechas = lista.mapNotNull { item ->
                val peso = (item["peso"] as? Number)?.toFloat()
                val fecha = (item["timestamp"] as? Timestamp)?.toDate()
                if (peso != null && fecha != null) peso to formato.format(fecha) else null
            }

            Result.success(pesos to pesosConFechas)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun updateWeight(nuevoPeso: Float): Result<UpdateWeightResult> {
        val user = auth.currentUser ?: return Result.failure(Exception("Usuario no autenticado"))
        val userRef = firestore.collection("usuarios").document(user.uid)

        return try {
            val doc = userRef.get().await()
            val historialRaw = doc.get("weightHistory") as? List<*>
            val historial = historialRaw?.filterIsInstance<Map<String, Any>>() ?: emptyList()

            val hoy = Calendar.getInstance()
            val listaActualizada = historial.toMutableList()
            var actualizado = false

            for (i in historial.indices) {
                val item = historial[i]
                val fecha = (item["timestamp"] as? Timestamp)?.toDate() ?: continue
                val cal = Calendar.getInstance().apply { time = fecha }
                val esHoy = hoy.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                        hoy.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)
                if (esHoy) {
                    listaActualizada[i] = mapOf(
                        "peso" to nuevoPeso,
                        "timestamp" to Timestamp.now()
                    )
                    actualizado = true
                    break
                }
            }

            if (!actualizado) {
                listaActualizada.add(
                    mapOf(
                        "peso" to nuevoPeso,
                        "timestamp" to Timestamp.now()
                    )
                )
            }

            val nuevosDatos = mapOf(
                "peso" to nuevoPeso,
                "weightHistory" to listaActualizada
            )

            userRef.set(nuevosDatos, SetOptions.merge()).await()

            val formato = SimpleDateFormat("dd MMM", Locale.getDefault())
            val fechaHoy = formato.format(Timestamp.now().toDate())
            val mensaje = if (actualizado) {
                "Actualizamos peso de hoy - $fechaHoy"
            } else {
                "Peso guardado correctamente - $fechaHoy"
            }

            val pesoObjetivo = doc.getDouble("pesoObjetivo")?.toFloat()
            val notificar = pesoObjetivo != null && nuevoPeso <= pesoObjetivo

            Result.success(UpdateWeightResult(mensaje, notificar))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



}
