package com.tusalud.healthapp.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*

val Context.dataStore by preferencesDataStore(name = "desafios_prefs")

object DesafiosPreferences {
    private val LAST_RESET_DATE = stringPreferencesKey("last_reset_date")
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    suspend fun shouldResetDesafios(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        val today = formatter.format(Date())
        val lastReset = prefs[LAST_RESET_DATE]
        return today != lastReset
    }

    suspend fun updateResetDate(context: Context) {
        val today = formatter.format(Date())
        context.dataStore.edit { prefs ->
            prefs[LAST_RESET_DATE] = today
        }
    }
}

