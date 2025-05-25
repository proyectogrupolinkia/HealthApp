
// clase que indica el resultado de la actualizacion del peso
package com.tusalud.healthapp.domain.model

data class UpdateWeightResult(
    val mensajeSnackbar: String,
    val notificar: Boolean
)
