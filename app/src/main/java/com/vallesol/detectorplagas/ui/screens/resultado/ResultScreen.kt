package com.vallesol.detectorplagas.ui.screens.resultado

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vallesol.detectorplagas.ml.DetectionResult
import com.vallesol.detectorplagas.ui.navigation.PantallaConDrawer
import com.vallesol.detectorplagas.ui.navigation.Rutas

@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: ResultViewModel = viewModel()
) {
    val context = LocalContext.current
    var ubicacionInput by remember { mutableStateOf("") }

    // 🛰️ Lanzador para pedir permisos de ubicación en tiempo de ejecución
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.intentarCargarClima(context)
        }
    }

    // Al abrir la pantalla comprobamos y pedimos el permiso si aún no se ha otorgado
    LaunchedEffect(Unit) {
        val permisoCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permisoCheck != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    PantallaConDrawer(navController = navController, titulo = "Resultado del Análisis") { modifier ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // 📷 Foto analizada compacta (130.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(16.dp))
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
                                style = Stroke(width = 6f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 📝 Campo libre para que el usuario escriba el lote o ubicación exacta
                OutlinedTextField(
                    value = ubicacionInput,
                    onValueChange = { ubicacionInput = it },
                    label = { Text("Escriba el Lote donde se encontró la plaga") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 🌤️ Tarjeta de análisis climático con la zona GPS integrada como referencia
                TarjetaClimaAnalisis(
                    zonaGps = viewModel.ubicacionSugerida,
                    temperatura = viewModel.temperaturaClima,
                    humedad = viewModel.humedadClima,
                    mensajeClima = viewModel.recomendacionClima
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 🐛 Resultado de la plaga visible de inmediato sin scroll
                if (viewModel.resultados.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se detectaron plagas en esta imagen",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    TarjetaResultado(viewModel.resultados.first())
                }
            }

            // 💾 Botones de acción fijos en la parte inferior
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        viewModel.guardarReporte(ubicacionInput.ifBlank { "Sin especificar" })

                        android.widget.Toast.makeText(
                            context,
                            "¡Reporte guardado con éxito!",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()

                        navController.popBackStack()
                    },
                    enabled = viewModel.resultados.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Guardar reporte", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                        Text("Otra foto")
                    }

                    OutlinedButton(
                        onClick = { navController.navigate(Rutas.GuiaPlagas.ruta) },
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Filled.Book, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                        Text("Ver guía")
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaClimaAnalisis(
    zonaGps: String,
    temperatura: String,
    humedad: String,
    mensajeClima: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🌤️ Análisis Climático",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    if (zonaGps.isNotBlank()) {
                        Text(
                            text = "📍 Zona GPS: $zonaGps",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Text(
                    text = if (temperatura.isNotBlank()) "$temperatura | 💧 $humedad" else "Offline",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = mensajeClima.ifBlank { "Datos climáticos no disponibles sin conexión" },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
private fun TarjetaResultado(resultado: DetectionResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = resultado.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "Confianza: ${(resultado.confidence * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = resultado.nombreCientifico,
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            ) {
                Text(
                    text = "Agente recomendado: ${resultado.agenteRecomendado}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}