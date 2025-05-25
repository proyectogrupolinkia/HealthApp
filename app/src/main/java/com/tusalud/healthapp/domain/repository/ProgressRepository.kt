//Interfaz del repositorio de progreso del usuario.

package com.tusalud.healthapp.domain.repository

import com.tusalud.healthapp.domain.model.Progress
import com.tusalud.healthapp.domain.model.UpdateWeightResult

interface ProgressRepository {
    /**
     * Recupera el estado actual del progreso del usuario,
     * incluyendo peso actual, objetivo y demás métricas relacionadas.
     *
     * @return Result<Progress> que contiene los datos o un error.
     */
    suspend fun getProgress(): Result<Progress>
    /**
     * Obtiene el historial completo de peso del usuario.
     * Devuelve una lista de valores y una lista de pares (peso, fecha formateada).
     *
     * @return Result<Pair<List<Float>, List<Pair<Float, String>>>>
     */
    suspend fun getWeightHistory(): Result<Pair<List<Float>, List<Pair<Float, String>>>>
    /**
     * Registra un nuevo peso y actualiza el historial.
     * También puede devolver una indicación de si debe mostrarse una notificación.
     *
     * @param nuevoPeso El peso que se desea guardar.
     * @return Result<UpdateWeightResult> con mensaje para Snackbar y notificación opcional.
     */
    suspend fun updateWeight(nuevoPeso: Float): Result<UpdateWeightResult>

}