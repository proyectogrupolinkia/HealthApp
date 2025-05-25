/*Calse que sire ve para gestionar el reinicio diario de los desafíos del usuario
mediante almacenamiento local persistente usando Jetpack DataStore.*/

package com.tusalud.healthapp.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*

// Crea una instancia de DataStore para almacenar preferencias del usuario relacionadas con desafíos.
// Se define como extensión del Context para poder acceder desde cualquier parte.
val Context.dataStore by preferencesDataStore(name = "desafios_prefs")

/**
 * Objeto de utilidad para gestionar las preferencias relacionadas con los desafíos diarios.
 * Utiliza DataStore para guardar la fecha del último reinicio de desafíos.
 */
object DesafiosPreferences {

    // Clave usada para guardar la fecha del último reinicio de desafíos
    private val LAST_RESET_DATE = stringPreferencesKey("last_reset_date")

    // Formateador de fecha para guardar y comparar las fechas como strings
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * Verifica si los desafíos deben reiniciarse hoy.
     * Retorna true si la fecha actual es diferente a la última guardada.
     */
    suspend fun shouldResetDesafios(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        val today = formatter.format(Date())
        val lastReset = prefs[LAST_RESET_DATE]
        return today != lastReset
    }

    /**
     * Actualiza la fecha del último reinicio de desafíos a la fecha actual.
     */
    suspend fun updateResetDate(context: Context) {
        val today = formatter.format(Date())
        context.dataStore.edit { prefs ->
            prefs[LAST_RESET_DATE] = today
        }
    }
}
