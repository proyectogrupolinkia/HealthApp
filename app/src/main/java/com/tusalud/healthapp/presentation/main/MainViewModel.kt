package com.tusalud.healthapp.presentation.main

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tusalud.healthapp.domain.model.Progress
import com.tusalud.healthapp.domain.repository.ProgressRepository
import com.tusalud.healthapp.utils.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _progress = MutableStateFlow<Progress?>(null)
    val progress: StateFlow<Progress?> = _progress

    private val _pesos = MutableStateFlow<List<Float>>(emptyList())
    val pesos: StateFlow<List<Float>> = _pesos

    private val _pesosConFechas = MutableStateFlow<List<Pair<Float, String>>>(emptyList())
    val pesosConFechas: StateFlow<List<Pair<Float, String>>> = _pesosConFechas

    private val _displayName = MutableStateFlow("")
    val displayName: StateFlow<String> = _displayName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _pesoInicio = MutableStateFlow("")
    val pesoInicio: StateFlow<String> = _pesoInicio

    private val _pesoObjetivo = MutableStateFlow("")
    val pesoObjetivo: StateFlow<String> = _pesoObjetivo

    private val _edad = MutableStateFlow("")
    val edad: StateFlow<String> = _edad


    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    var snackbarActivo by mutableStateOf(false)
        private set

    var snackbarMensaje by mutableStateOf("")
        private set

    init {
        loadProgress()
        cargarPesosDesdeFirebase()
    }

    internal fun cargarPesosDesdeFirebase() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val listaRaw = document.get("weightHistory") as? List<*>
                val lista = listaRaw?.filterIsInstance<Map<String, Any>>()
                lista?.let {
                    val pesosFloat = it.mapNotNull { item ->
                        (item["peso"] as? Number)?.toFloat()
                    }
                    _pesos.value = pesosFloat

                    val formato = SimpleDateFormat("dd MMM", Locale.getDefault())
                    val pesosFechas = it.mapNotNull { item ->
                        val peso = (item["peso"] as? Number)?.toFloat()
                        val fecha = (item["timestamp"] as? Timestamp)?.toDate()
                        if (peso != null && fecha != null) peso to formato.format(fecha) else null
                    }
                    _pesosConFechas.value = pesosFechas
                }
            }
            .addOnFailureListener {
                _pesos.value = emptyList()
                _pesosConFechas.value = emptyList()
            }
    }

    fun loadProgress() {
        viewModelScope.launch {
            _progress.value = progressRepository.getProgress()
        }
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
                    snackbarMensaje = if (actualizado) {
                        "Actualizamos peso de hoy - $fechaHoyFormateada"
                    } else {
                        "Peso guardado correctamente - $fechaHoyFormateada"
                    }
                    snackbarActivo = true
                    loadProgress()
                    cargarPesosDesdeFirebase()

                    val objetivo = doc.getDouble("pesoObjetivo")?.toFloat()

                    if (objetivo != null && nuevoPeso <= objetivo) {
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

    fun cargarDatosUsuarioCompleto() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val uid = user.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                val peso = document.getDouble("peso")?.toFloat()
                val altura = document.getDouble("altura")?.toFloat()
                val bmi = if (peso != null && altura != null && altura > 0)
                    peso / ((altura / 100) * (altura / 100)) else 0f

                _progress.value = Progress(
                    peso = peso ?: 0f,
                    heightCm = altura ?: 0f,
                    bmi = bmi,
                    pesoObjetivo = document.getDouble("pesoObjetivo")?.toFloat()
                )

                val historialRaw = document.get("weightHistory") as? List<*>
                val historial = historialRaw?.filterIsInstance<Map<String, Any>>() ?: emptyList()
                val formato = SimpleDateFormat("dd MMM", Locale.getDefault())

                _pesos.value = historial.mapNotNull { (it["peso"] as? Number)?.toFloat() }
                _pesosConFechas.value = historial.mapNotNull { item ->
                    val pesoItem = (item["peso"] as? Number)?.toFloat()
                    val fecha = (item["timestamp"] as? Timestamp)?.toDate()
                    if (pesoItem != null && fecha != null) pesoItem to formato.format(fecha) else null
                }

                _pesoInicio.value = document.getDouble("pesoInicio")?.toString() ?: ""
                _pesoObjetivo.value = document.getDouble("pesoObjetivo")?.toString() ?: ""
                _displayName.value = document.getString("nombre") ?: ""
                _email.value = user.email ?: ""
                _edad.value = document.getLong("edad")?.toString() ?: ""

            }
            .addOnFailureListener {
                println("Error al cargar los datos del usuario: ${it.message}")
            }
    }

    fun resetSnackbar() {
        snackbarActivo = false
    }

    fun onDisplayNameChanged(newName: String) {
        _displayName.value = newName
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPesoInicioChanged(newPeso: String) {
        _pesoInicio.value = newPeso
    }

    fun onPesoObjetivoChanged(newPeso: String) {
        _pesoObjetivo.value = newPeso
    }


    fun onEdadChanged(nuevaEdad: String) {
        _edad.value = nuevaEdad
    }

    fun isEdadValida(): Boolean {
        return _edad.value.toIntOrNull()?.let { it in 1..120 } ?: false
    }
    fun isPesoValido(peso: String): Boolean {
        return peso.toFloatOrNull()?.let { it in 1f..500f } ?: false
    }


    fun updateProfile(onSuccess: () -> Unit = {}) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val newName = _displayName.value
        val newEmail = _email.value
        val pesoIni = _pesoInicio.value.toFloatOrNull()
        val pesoObj = _pesoObjetivo.value.toFloatOrNull()

        viewModelScope.launch {
            if (newName != user.displayName) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = newName
                }
                user.updateProfile(profileUpdates)
            }

            if (newEmail != user.email) {
                user.updateEmail(newEmail)
            }

            val datosPerfil = mutableMapOf<String, Any>(
                "nombre" to newName,
                "email" to newEmail
            )
            pesoIni?.let { datosPerfil["pesoInicio"] = it }
            pesoObj?.let { datosPerfil["pesoObjetivo"] = it }
            val edadInt = _edad.value.toIntOrNull()
            edadInt?.let { datosPerfil["edad"] = it }

            FirebaseFirestore.getInstance().collection("usuarios").document(user.uid)
                .set(datosPerfil, SetOptions.merge())
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _toastMessage.emit("Perfil actualizado con éxito")
                        onSuccess()
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _toastMessage.emit("Error al guardar perfil")
                    }
                }
        }
    }
}
