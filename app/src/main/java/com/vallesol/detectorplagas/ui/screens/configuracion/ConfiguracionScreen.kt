package com.vallesol.detectorplagas.ui.screens.configuracion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vallesol.detectorplagas.ui.navigation.PantallaConDrawer

@Composable
fun ConfiguracionScreen(
    navController: NavController,
    viewModel: ConfiguracionViewModel = viewModel()
) {
    val guardarFotos by viewModel.guardarFotosAutomatico.collectAsState()
    val calidad by viewModel.calidadCaptura.collectAsState()
    val modoOscuro by viewModel.modoOscuro.collectAsState()
    val tamanoFuente by viewModel.tamanoFuente.collectAsState()

    var mostrarDialogoBorrar by remember { mutableStateOf(false) }

    PantallaConDrawer(navController = navController, titulo = "Configuración") { modifier ->
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Sección 1: Cámara
            item {
                TarjetaSeccion(
                    titulo = "Cámara",
                    icono = Icons.Filled.PhotoCamera,
                    colorAcento = MaterialTheme.colorScheme.primary
                ) {
                    FilaSwitch(
                        titulo = "Guardar fotos automáticamente",
                        valor = guardarFotos,
                        onCambio = viewModel::setGuardarFotosAutomatico
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    FilaDropdown(
                        titulo = "Calidad de captura",
                        opciones = listOf("baja" to "Baja", "media" to "Media", "alta" to "Alta"),
                        valorActual = calidad,
                        onSeleccion = viewModel::setCalidadCaptura
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Sección 2: Apariencia
            item {
                TarjetaSeccion(
                    titulo = "Apariencia",
                    icono = Icons.Filled.Palette,
                    colorAcento = MaterialTheme.colorScheme.tertiary
                ) {
                    FilaSwitch(
                        titulo = "Modo oscuro",
                        valor = modoOscuro,
                        onCambio = viewModel::setModoOscuro
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    FilaDropdown(
                        titulo = "Tamaño de fuente",
                        opciones = listOf("pequena" to "Pequeña", "normal" to "Normal", "grande" to "Grande"),
                        valorActual = tamanoFuente,
                        onSeleccion = viewModel::setTamanoFuente
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Sección 3: Datos locales — fila de acción destructiva unificada
            item {
                TarjetaSeccion(
                    titulo = "Datos locales",
                    icono = Icons.Filled.Delete,
                    colorAcento = MaterialTheme.colorScheme.error
                ) {
                    Text(
                        text = "Elimina los registros almacenados localmente en el dispositivo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { mostrarDialogoBorrar = true },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Borrar historial de detecciones",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoBorrar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoBorrar = false },
            title = { Text("Borrar historial") },
            text = { Text("¿Seguro que quieres eliminar todas las fotos y resultados guardados? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.borrarHistorial()
                        mostrarDialogoBorrar = false
                    }
                ) {
                    Text("Borrar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoBorrar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TarjetaSeccion(
    titulo: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    colorAcento: Color,
    contenido: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 14.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = colorAcento.copy(alpha = 0.15f),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = colorAcento,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            contenido()
        }
    }
}

@Composable
private fun FilaSwitch(titulo: String, valor: Boolean, onCambio: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Switch(checked = valor, onCheckedChange = onCambio)
    }
}

@Composable
private fun FilaDropdown(
    titulo: String,
    opciones: List<Pair<String, String>>,
    valorActual: String,
    onSeleccion: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    val etiquetaActual = opciones.firstOrNull { it.first == valorActual }?.second ?: valorActual

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )

        Box {
            // El valor actual ahora es un chip, no una flechita de texto suelta
            Surface(
                modifier = Modifier.clickable { expandido = true },
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = etiquetaActual,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                opciones.forEach { (valor, etiqueta) ->
                    DropdownMenuItem(
                        text = { Text(etiqueta) },
                        onClick = {
                            onSeleccion(valor)
                            expandido = false
                        }
                    )
                }
            }
        }
    }
}