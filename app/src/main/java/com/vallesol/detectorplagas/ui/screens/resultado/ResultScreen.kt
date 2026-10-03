package com.vallesol.detectorplagas.ui.screens.resultado

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vallesol.detectorplagas.ui.navigation.PantallaConDrawer

@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: ResultViewModel = viewModel()
) {
    PantallaConDrawer(navController = navController, titulo = "Resultado del Análisis") { modifier ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Contenedor visual con la imagen analizada y el overlay de detecciones
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                viewModel.bitmap?.let { bmp ->
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Foto analizada",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Canvas(modifier = Modifier.fillMaxSize()) {
                    viewModel.resultados.forEach { resultado ->
                        val box = resultado.boundingBox
                        val ancho = size.width
                        val alto = size.height

                        val izquierda = box.left * ancho
                        val arriba = box.top * alto
                        val derecha = box.right * ancho
                        val abajo = box.bottom * alto

                        drawRect(
                            color = Color(0xFF2E7D32),
                            topLeft = Offset(izquierda, arriba),
                            size = Size(derecha - izquierda, abajo - arriba),
                            style = Stroke(width = 8f)
                        )
                    }
                }
            }
        }
    }
}