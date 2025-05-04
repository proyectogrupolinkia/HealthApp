package com.tusalud.healthapp.util

import kotlin.math.log10

fun calcularIMC(peso: Float, altura: Float): Float {
    return peso / (altura * altura)
}

fun calcularGrasaCorporal(
    cintura: Float,
    cuello: Float,
    altura: Float,
    cadera: Float?,
    esHombre: Boolean
): Float {
    return if (esHombre) {
        495f / (1.0324f - 0.19077f * log10((cintura - cuello).toDouble()) + 0.15456f * log10(altura.toDouble())).toFloat() - 450f
    } else {
        495f / (1.29579f - 0.35004f * log10((cintura + (cadera ?: 0f) - cuello).toDouble()) + 0.221f * log10(altura.toDouble())).toFloat() - 450f
    }
}