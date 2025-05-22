
package com.tusalud.healthapp.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

object NotificationHelper {

    private const val CHANNEL_ID = "progreso_channel"

    private fun yaSeMostroNotificacionHoy(context: Context): Boolean {
        val prefs = context.getSharedPreferences("prefs_notificaciones", Context.MODE_PRIVATE)
        val hoy = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        return prefs.getString("ultimo_dia_notificado", "") == hoy
    }

    private fun marcarNotificacionMostradaHoy(context: Context) {
        val prefs = context.getSharedPreferences("prefs_notificaciones", Context.MODE_PRIVATE)
        val hoy = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        prefs.edit().putString("ultimo_dia_notificado", hoy).apply()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun mostrarNotificacionObjetivo(context: Context) {
        if (yaSeMostroNotificacionHoy(context)) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Progreso de Salud",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("¡Felicidades!")
            .setContentText("Has alcanzado tu peso objetivo.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(1001, builder.build())
        }

        marcarNotificacionMostradaHoy(context)
    }
}
