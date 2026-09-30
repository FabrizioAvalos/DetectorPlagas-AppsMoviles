package com.vallesol.detectorplagas.ui.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.vallesol.detectorplagas.ui.navigation.PantallaConDrawer

@Composable
fun HomeScreen(navController: NavController) {
    PantallaConDrawer(navController = navController, titulo = "DetectorPlagas") { modifier ->
        Text("Pantalla Home (pendiente)", modifier = modifier)
    }
}