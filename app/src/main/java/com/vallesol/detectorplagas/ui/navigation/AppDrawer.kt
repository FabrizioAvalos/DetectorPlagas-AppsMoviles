package com.vallesol.detectorplagas.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

data class ItemMenu(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
)

val itemsDrawer = listOf(
    ItemMenu(Rutas.Home.ruta, "Inicio", Icons.Filled.Home),
    ItemMenu(Rutas.GuiaPlagas.ruta, "Guía", Icons.AutoMirrored.Filled.MenuBook),
    ItemMenu(Rutas.Detectar.ruta, "Detectar", Icons.Filled.CameraAlt),
    ItemMenu(Rutas.Reportes.ruta, "Reportes", Icons.Filled.Assessment),
    ItemMenu(Rutas.Configuracion.ruta, "Ajustes", Icons.Filled.Settings)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConDrawer(
    navController: NavController,
    titulo: String,
    contenido: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(titulo) })
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    itemsDrawer.forEach { item ->
                        val seleccionado = currentRoute == item.ruta
                        Icon(
                            imageVector = item.icono,
                            contentDescription = item.titulo,
                            tint = if (seleccionado) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        contenido(Modifier.padding(innerPadding))
    }
}