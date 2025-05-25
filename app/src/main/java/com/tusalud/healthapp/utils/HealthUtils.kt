
//clase  que contiene funciones para calcular indicadores de salud
package com.tusalud.healthapp.utils

import kotlin.math.log10


/**
 * Calcula el Índice de Masa Corporal (IMC) según la fórmula estándar.
 *
 * @param peso Peso en kilogramos.
 * @param altura Altura en metros.
 * @return IMC como un valor Float.
 */

fun calcularIMC(peso: Float, altura: Float): Float {
    return peso / (altura * altura)
}
/**
 * Calcula el porcentaje estimado de grasa corporal usando la fórmula de la Marina de EE.UU.
 *
 * @param cintura Circunferencia de la cintura (en cm).
 * @param cuello Circunferencia del cuello (en cm).
 * @param altura Altura del usuario (en cm).
 * @param cadera Circunferencia de la cadera (solo se usa si es mujer).
 * @param esHombre Booleano que indica si el usuario es hombre.
 * @return Porcentaje estimado de grasa corporal como Float.
 */

fun calcularGrasaCorporal(
    cintura: Float,
    cuello: Float,
    altura: Float,
    cadera: Float?,
    esHombre: Boolean
): Float {
    return if (esHombre) {
        // Fórmula para hombres

        495f / (1.0324f - 0.19077f * log10((cintura - cuello).toDouble()) + 0.15456f * log10(altura.toDouble())).toFloat() - 450f
    } else {
        // Fórmula para mujeres (usa también la cadera)

        495f / (1.29579f - 0.35004f * log10((cintura + (cadera ?: 0f) - cuello).toDouble()) + 0.221f * log10(altura.toDouble())).toFloat() - 450f
    }
}