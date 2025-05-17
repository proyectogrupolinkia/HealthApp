package com.tusalud.healthapp.presentation.menu.meditacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class MeditacionViewModel : ViewModel(){

    // Estado de la pantalla
    private val _estado = MutableStateFlow<MeditacionEstado>(MeditacionEstado.Cargando)
    val estado: StateFlow<MeditacionEstado> = _estado

    // iniciar con el delay
    init {
        cargarEjercicios()
    }


    /** Función que muestra un pequeño delay para acto seguido, cargar un conjunto de
     * ejercicios predefinidos por el usuario.
     * @author Alejandro
     * */
     fun cargarEjercicios() {
        viewModelScope.launch {
            // Simulamos una carga demorada (como una API o BD)
            kotlinx.coroutines.delay(1000)

            val ejercicios = listOf(
                EjercicioMeditacion(
                    id = 1,
                    nombre = "Respiración Consciente",
                    descripcion = "Enfócate en el flujo natural de tu respiración",
                    duracionMinutos = 5,
                    imagenUrl = "https://example.com/icon1.png"
                ),
                EjercicioMeditacion(
                    id = 2,
                    nombre = "Body Scan",
                    descripcion = "Recorre mentalmente cada parte de tu cuerpo liberando tensiones",
                    duracionMinutos = 10,
                    imagenUrl = "https://example.com/icon2.png"
                ),
                EjercicioMeditacion(
                    id = 3,
                    nombre = "Meditación Guiada",
                    descripcion = "Sigue una voz que te guiará a un estado de relajación profunda",
                    duracionMinutos = 15,
                    imagenUrl = "https://example.com/icon3.png"
                ),
                EjercicioMeditacion(
                    id = 4,
                    nombre = "Mindfulness",
                    descripcion = "Observa tus pensamientos sin juzgarlos, como nubes pasajeras",
                    duracionMinutos = 8,
                    imagenUrl = "https://example.com/icon4.png"
                ),
                EjercicioMeditacion(
                    id = 5,
                    nombre = "Metta (Amor Benevolente)",
                    descripcion = "Cultiva sentimientos de amor hacia ti y los demás",
                    duracionMinutos = 12,
                    imagenUrl = "https://example.com/icon5.png"
                ),
                EjercicioMeditacion(
                    id = 6,
                    nombre = "Visualización Creativa",
                    descripcion = "Imagina un lugar seguro que te transmita paz interior",
                    duracionMinutos = 7,
                    imagenUrl = "https://example.com/icon6.png"
                ),
                EjercicioMeditacion(
                    id = 7,
                    nombre = "Zazen",
                    descripcion = "Meditación zen tradicional, enfocada en la postura y la respiración",
                    duracionMinutos = 20,
                    imagenUrl = "https://example.com/icon7.png"
                ),
                EjercicioMeditacion(
                    id = 8,
                    nombre = "Sonido Primordial",
                    descripcion = "Usa mantras para alcanzar estados elevados de conciencia",
                    duracionMinutos = 10,
                    imagenUrl = "https://example.com/icon8.png"
                )
            )

            _estado.value = MeditacionEstado.Exito(ejercicios)
        }
    }

    // Estados posibles de la pantalla
    sealed class MeditacionEstado {
        object Cargando : MeditacionEstado()
        data class Exito(val ejercicios: List<EjercicioMeditacion>) : MeditacionEstado()
        data class Error(val mensaje: String) : MeditacionEstado()
    }
}