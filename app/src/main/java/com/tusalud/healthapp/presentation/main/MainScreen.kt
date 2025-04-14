package com.tusalud.healthapp.presentation.main

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tusalud.healthapp.domain.model.User

@Composable
fun MainScreen(user: User) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Bienvenido, ${user.nombre}")
        Text("Correo: ${user.correo}")
        Text("Edad: ${user.edad}")
        Text("Peso: ${user.peso}")
        Text("Altura: ${user.altura}")
    }
}