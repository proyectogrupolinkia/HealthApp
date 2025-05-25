package com.tusalud.healthapp

// Importa la clase base Application del framework Android
import android.app.Application
// Impoorta hilt para la inyeccion de dependencias.
import dagger.hilt.android.HiltAndroidApp

//Punto de entrada global de la aplicación
@HiltAndroidApp
class MyApp : Application()
