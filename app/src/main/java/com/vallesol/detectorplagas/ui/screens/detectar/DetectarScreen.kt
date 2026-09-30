package com.vallesol.detectorplagas.ui.screens.detectar

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.vallesol.detectorplagas.ui.navigation.PantallaConDrawer

@Composable
fun DetectarScreen(navController: NavController) {
    PantallaConDrawer(navController = navController, titulo = "Detectar") { modifier ->
        Text("Pantalla Detectar (pendiente)", modifier = modifier)
    }
}