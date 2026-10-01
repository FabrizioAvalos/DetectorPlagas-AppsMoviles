package com.vallesol.detectorplagas.ui.screens.guiaplagas

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vallesol.detectorplagas.data.local.ControladorBiologico
import com.vallesol.detectorplagas.ui.navigation.PantallaConDrawer

@Composable
fun GuiaPlagasScreen(
    navController: NavController,
    viewModel: GuiaPlagasViewModel = viewModel()
) {
    val controladores by viewModel.controladores.collectAsStateWithLifecycle()

    PantallaConDrawer(navController = navController, titulo = "Guía de Controladores") { modifier ->
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(controladores) { controlador ->
                TarjetaControladorBasica(controlador)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun TarjetaControladorBasica(item: ControladorBiologico) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = item.nombreComun,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}