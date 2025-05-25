
// Caso de uso responsable de actualizar el peso del usuario.


package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.model.UpdateWeightResult
import com.tusalud.healthapp.domain.repository.ProgressRepository
import javax.inject.Inject


/**
 * Ejecuta la operación de actualización de peso.
 * @param nuevoPeso El nuevo valor de peso que se desea registrar.
 * @return Un Result que puede contener un objeto UpdateWeightResult (éxito) o una excepción (error).
 */
class UpdateWeightUseCase @Inject constructor(
    private val repository: ProgressRepository
) {
    suspend operator fun invoke(nuevoPeso: Float): Result<UpdateWeightResult> {
        return repository.updateWeight(nuevoPeso)
    }
}
