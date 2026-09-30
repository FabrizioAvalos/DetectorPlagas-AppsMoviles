package com.vallesol.detectorplagas.ui.screens.configuracion

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.vallesol.detectorplagas.ui.navigation.PantallaConDrawer

@Composable
fun ConfiguracionScreen(navController: NavController) {
    PantallaConDrawer(navController = navController, titulo = "Configuración") { modifier ->
        Text("Pantalla Configuración (pendiente)", modifier = modifier)
    }
}