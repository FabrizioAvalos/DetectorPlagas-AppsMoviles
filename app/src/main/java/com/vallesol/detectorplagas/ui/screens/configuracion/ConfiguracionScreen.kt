package com.vallesol.detectorplagas.ui.screens.configuracion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val modoOscuro by viewModel.modoOscuro.collectAsState()
    val tamanoFuente by viewModel.tamanoFuente.collectAsState()

    PantallaConDrawer(navController = navController, titulo = "Configuración") { modifier ->
        Column_(modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Modo oscuro",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Switch(checked = modoOscuro, onCheckedChange = viewModel::setModoOscuro)
            }

            FilaDropdownTamanoFuente(
                valorActual = tamanoFuente,
                onSeleccion = viewModel::setTamanoFuente
            )
        }
    }
}

@Composable
private fun Column_(modifier: Modifier, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    androidx.compose.foundation.layout.Column(modifier = modifier.fillMaxWidth(), content = content)
}

@Composable
private fun FilaDropdownTamanoFuente(valorActual: String, onSeleccion: (String) -> Unit) {
    var expandido by remember { mutableStateOf(false) }
    val opciones = listOf("pequena" to "Pequeña", "normal" to "Normal", "grande" to "Grande")
    val etiquetaActual = opciones.firstOrNull { it.first == valorActual }?.second ?: valorActual

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Tamaño de fuente",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        Box {
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

            DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
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