package com.vallesol.detectorplagas.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
    actions: @Composable RowScope.() -> Unit = {},
    contenido: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo) },
                actions = actions
            )
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Barra de navegación inferior (Superficie blanca)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp,
                    tonalElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ItemBarraNavegacion(
                            item = itemsDrawer[0],
                            seleccionado = currentRoute == itemsDrawer[0].ruta,
                            navController = navController
                        )
                        ItemBarraNavegacion(
                            item = itemsDrawer[1],
                            seleccionado = currentRoute == itemsDrawer[1].ruta,
                            navController = navController
                        )

                        // Espacio para la cámara flotante central
                        Box(modifier = Modifier.weight(1f).size(72.dp))

                        ItemBarraNavegacion(
                            item = itemsDrawer[3],
                            seleccionado = currentRoute == itemsDrawer[3].ruta,
                            navController = navController
                        )
                        ItemBarraNavegacion(
                            item = itemsDrawer[4],
                            seleccionado = currentRoute == itemsDrawer[4].ruta,
                            navController = navController
                        )
                    }
                }

                // Botón central flotante de "Detectar"
                val detectarSeleccionado = currentRoute == Rutas.Detectar.ruta
                val primaryColor = MaterialTheme.colorScheme.primary

                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-24).dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (!detectarSeleccionado) {
                                navController.navigate(Rutas.Detectar.ruta) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        .padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .shadow(elevation = 6.dp, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            shape = CircleShape,
                            color = primaryColor,
                            tonalElevation = 4.dp
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CameraAlt,
                                    contentDescription = "Detectar plaga",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = "Detectar",
                        color = if (detectarSeleccionado) primaryColor else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    ) { innerPadding ->
        contenido(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun RowScope.ItemBarraNavegacion(
    item: ItemMenu,
    seleccionado: Boolean,
    navController: NavController
) {
    val colorContenido = if (seleccionado) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    // Fondo tipo "pill" que aparece/desaparece con una transición suave,
    // mismo lenguaje de acento de color que los badges de Home/Configuración/Reportes
    val colorFondo by animateColorAsState(
        targetValue = if (seleccionado) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        } else {
            Color.Transparent
        },
        label = "colorFondoNavItem"
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (navController.currentDestination?.route != item.ruta) {
                    navController.navigate(item.ruta) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
            .padding(vertical = 4.dp, horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = colorFondo
        ) {
            Icon(
                imageVector = item.icono,
                contentDescription = item.titulo,
                tint = colorContenido,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .size(22.dp)
            )
        }

        Text(
            text = item.titulo,
            color = colorContenido,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}