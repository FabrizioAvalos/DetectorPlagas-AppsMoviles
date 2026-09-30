package com.vallesol.detectorplagas.ui.screens.resultado

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.vallesol.detectorplagas.ui.navigation.PantallaConDrawer

@Composable
fun ResultScreen(navController: NavController) {
    PantallaConDrawer(navController = navController, titulo = "Resultado") { modifier ->
        Text("Pantalla Resultado (pendiente)", modifier = modifier)
    }
}