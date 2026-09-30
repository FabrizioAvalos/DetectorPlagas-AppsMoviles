package com.vallesol.detectorplagas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vallesol.detectorplagas.ui.screens.configuracion.ConfiguracionScreen
import com.vallesol.detectorplagas.ui.screens.detectar.DetectarScreen
import com.vallesol.detectorplagas.ui.screens.guiaplagas.GuiaPlagasScreen
import com.vallesol.detectorplagas.ui.screens.home.HomeScreen
import com.vallesol.detectorplagas.ui.screens.reportes.ReportesScreen
import com.vallesol.detectorplagas.ui.screens.resultado.ResultScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.Home.ruta) {
        composable(Rutas.Home.ruta) { HomeScreen(navController) }
        composable(Rutas.Detectar.ruta) { DetectarScreen(navController) }
        composable(Rutas.Resultado.ruta) { ResultScreen(navController) }
        composable(Rutas.GuiaPlagas.ruta) { GuiaPlagasScreen(navController) }
        composable(Rutas.Reportes.ruta) { ReportesScreen(navController) }
        composable(Rutas.Configuracion.ruta) { ConfiguracionScreen(navController) }
    }
}