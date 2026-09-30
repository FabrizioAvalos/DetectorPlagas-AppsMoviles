package com.vallesol.detectorplagas.ui.screens.guiaplagas

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.vallesol.detectorplagas.ui.navigation.PantallaConDrawer

@Composable
fun GuiaPlagasScreen(navController: NavController) {
    PantallaConDrawer(navController = navController, titulo = "Guía de Controladores") { modifier ->
        Text("Pantalla Guía de Plagas (pendiente)", modifier = modifier)
    }
}