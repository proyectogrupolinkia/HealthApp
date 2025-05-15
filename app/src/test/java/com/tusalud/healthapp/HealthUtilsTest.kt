package com.tusalud.healthapp

import com.tusalud.healthapp.utils.calcularIMC
import com.tusalud.healthapp.utils.calcularGrasaCorporal
import org.junit.Assert.assertEquals
import org.junit.Test

class HealthUtilsTest {

    @Test
    fun testCalcularIMC() {
        val imc = calcularIMC(70f, 1.75f)
        assertEquals(22.86f, imc, 0.01f)  //margen de error permitido
    }

    @Test
    fun testGrasaCorporalHombre() {
        val grasa = calcularGrasaCorporal(85f, 40f, 175f, null, true)
        assertEquals(18.0f, grasa, 2.0f) //resultado aproximado
    }

    @Test
    fun testGrasaCorporalMujer() {
        val grasa = calcularGrasaCorporal(70f, 30f, 165f, 95f, false)
        assertEquals(28.0f, grasa, 2.0f)
    }
}
