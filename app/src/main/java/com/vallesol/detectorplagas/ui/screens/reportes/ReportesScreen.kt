package com.vallesol.detectorplagas.ui.screens.reportes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.vallesol.detectorplagas.ui.navigation.PantallaConDrawer

@Composable
fun ReportesScreen(navController: NavController) {
    PantallaConDrawer(navController = navController, titulo = "Reportes") { modifier ->
        Text("Pantalla Reportes (pendiente)", modifier = modifier)
    }
}