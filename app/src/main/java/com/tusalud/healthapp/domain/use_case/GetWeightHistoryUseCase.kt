package com.tusalud.healthapp.domain.use_case


import com.tusalud.healthapp.domain.repository.ProgressRepository
import javax.inject.Inject

//Caso de uso que recupera el historial de peso del usuario.

class GetWeightHistoryUseCase @Inject constructor(
    private val repository: ProgressRepository
) {

    /**
     * Llama al repositorio para obtener el historial de pesos.
     *
     * @return Result con un par de listas:
     *   - Lista de pesos (Float)
     *   - Lista de pares (peso, fecha formateada)
     */
    suspend operator fun invoke(): Result<Pair<List<Float>, List<Pair<Float, String>>>> {
        return repository.getWeightHistory()
    }
}
