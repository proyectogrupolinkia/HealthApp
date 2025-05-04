package com.tusalud.healthapp.presentation.menu

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

@Composable
fun EditarPerfilScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    var newDisplayName by remember { mutableStateOf(auth.currentUser?.displayName ?: "") }
    var newEmail by remember { mutableStateOf(auth.currentUser?.email ?: "") }
    var isEditing by remember { mutableStateOf(false) }

    //funcion para actualizar el nombre de usuario y el email
    fun updateProfile() {
        val user = auth.currentUser

        if (user != null) {

            if (newDisplayName != user.displayName) {
                user.updateProfile(userProfileChangeRequest {
                    displayName = newDisplayName
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            navController.context,
                            "Nombre de usuario actualizado correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            navController.context,
                            "Error al actualizar nombre de usuario",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }


            if (newEmail != user.email) {
                user.updateEmail(newEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            navController.context,
                            "Correo electrónico actualizado correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            navController.context,
                            "Error al actualizar correo electrónico",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            Toast.makeText(
                navController.context,
                "Usuario no encontrado",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp)) // Espacio superior

        Text(
            "Editar Perfil",
            fontSize = 24.sp,
            color = Color(0xFF00C6A7),
            modifier = Modifier.padding(bottom = 16.dp)
        )


        OutlinedTextField(
            value = newDisplayName,
            onValueChange = { newDisplayName = it },
            label = { Text("Nombre de usuario") },
            enabled = isEditing,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = newEmail,
            onValueChange = { newEmail = it },
            label = { Text("Correo electrónico") },
            enabled = isEditing,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Button(
                onClick = {
                    if (isEditing) {
                        //guardar los cambios si se edito
                        updateProfile()
                    }
                    isEditing = !isEditing
                }
            ) {
                Text(if (isEditing) "Guardar cambios" else "Editar perfil")
            }
        }
    }
}


