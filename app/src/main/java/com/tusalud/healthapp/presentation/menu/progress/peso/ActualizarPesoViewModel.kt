
package com.tusalud.healthapp.presentation.menu.progress.peso

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tusalud.healthapp.utils.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ActualizarPesoViewModel @Inject constructor() : ViewModel() {

    private val _snackbarActivo = MutableStateFlow(false)
    val snackbarActivo: StateFlow<Boolean> = _snackbarActivo

    private val _snackbarMensaje = MutableStateFlow("")
    val snackbarMensaje: StateFlow<String> = _snackbarMensaje

    fun resetSnackbar() {
        _snackbarActivo.value = false
    }

    fun actualizarPeso(context: Context, nuevoPeso: Float?) {
        if (nuevoPeso == null) return

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("usuarios").document(userId)

        userRef.get().addOnSuccessListener { doc ->
            val historialRaw = doc.get("weightHistory") as? List<*>
            val historial = historialRaw?.filterIsInstance<Map<String, Any>>() ?: emptyList()

            val hoy = Calendar.getInstance()
            val listaActualizada = historial.toMutableList()
            var actualizado = false

            for (i in historial.indices) {
                val item = historial[i]
                val fecha = (item["timestamp"] as? Timestamp)?.toDate() ?: continue
                val cal = Calendar.getInstance().apply { time = fecha }

                val esHoy = hoy.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                        hoy.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)

                if (esHoy) {
                    listaActualizada[i] = mapOf(
                        "peso" to nuevoPeso,
                        "timestamp" to Timestamp.now()
                    )
                    actualizado = true
                    break
                }
            }

            if (!actualizado) {
                listaActualizada.add(
                    mapOf(
                        "peso" to nuevoPeso,
                        "timestamp" to Timestamp.now()
                    )
                )
            }

            val nuevosDatos = mapOf(
                "peso" to nuevoPeso,
                "weightHistory" to listaActualizada
            )

            userRef.set(nuevosDatos, SetOptions.merge())
                .addOnSuccessListener {
                    val formato = SimpleDateFormat("dd MMM", Locale.getDefault())
                    val fechaHoyFormateada = formato.format(Timestamp.now().toDate())
                    _snackbarMensaje.value = if (actualizado) {
                        "Actualizamos peso de hoy - $fechaHoyFormateada"
                    } else {
                        "Peso guardado correctamente - $fechaHoyFormateada"
                    }
                    _snackbarActivo.value = true

                    val objetivo = doc.getDouble("pesoObjetivo")?.toFloat()

                    val tienePermiso = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                            ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED

                    if (objetivo != null && nuevoPeso <= objetivo && tienePermiso) {
                        NotificationHelper.mostrarNotificacionObjetivo(context)
                    }
                }
                .addOnFailureListener {
                    println("Error al actualizar peso: ${it.message}")
                }
        }.addOnFailureListener {
            println("Error al obtener documento del usuario: ${it.message}")
        }
    }
}
