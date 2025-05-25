//Caso de uso que recupera el estado actual del progreso del usuario.
package com.tusalud.healthapp.domain.use_case

import com.tusalud.healthapp.domain.model.Progress
import com.tusalud.healthapp.domain.repository.ProgressRepository
import javax.inject.Inject

class GetProgressUseCase @Inject constructor(
    private val repository: ProgressRepository
) {

    /**
     * Llama al repositorio para obtener el progreso actual del usuario.
     *
     * @return Result<Progress> que contiene los datos de progreso o un error.
     */
    suspend operator fun invoke(): Result<Progress> {
        return repository.getProgress()
    }
}
