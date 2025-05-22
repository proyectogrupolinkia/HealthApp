package com.tusalud.healthapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tusalud.healthapp.R

object NotificationHelper {

    private const val CHANNEL_ID = "progreso_channel"

    fun mostrarNotificacionObjetivo(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notificaciones de progreso",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para avisar cuando se alcanza el peso objetivo"
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Ícono genérico
            .setContentTitle("¡Felicidades!")
            .setContentText("Has alcanzado o superado tu peso objetivo.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // ✅ Validación segura del permiso antes de mostrar
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(1001, builder.build())
        }
    }
}
