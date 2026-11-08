package com.vallesol.detectorplagas.ui.screens.resultado

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.vallesol.detectorplagas.Reporte
import com.vallesol.detectorplagas.ReportesRepository
import com.vallesol.detectorplagas.ResultadoTemporal
import com.vallesol.detectorplagas.ml.DetectionResult
import com.vallesol.detectorplagas.network.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.Manifest
class ResultViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReportesRepository(application)
    private val weatherApi = WeatherApiService.crear()
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    val bitmap = ResultadoTemporal.bitmap
    val resultados: List<DetectionResult> = ResultadoTemporal.resultados

    var temperaturaClima by mutableStateOf("")
        private set

    var humedadClima by mutableStateOf("")
        private set

    var recomendacionClima by mutableStateOf("Datos climáticos no disponibles sin conexión")
        private set

    // 🌍 Variable para sugerir automáticamente la ubicación detectada por GPS
    var ubicacionSugerida by mutableStateOf("")
        private set

    init {
        intentarCargarClima(application)
    }

    @SuppressLint("MissingPermission")
    fun intentarCargarClima(context: Context) {
        val permisoFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val permisoCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (permisoFine == PackageManager.PERMISSION_GRANTED || permisoCoarse == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    // 1. Consultamos el clima con las coordenadas
                    consultarApiClima(location.latitude, location.longitude)

                    // 2. Traducimos las coordenadas a nombre de lugar usando Geocoder
                    obtenerNombreUbicacion(context, location.latitude, location.longitude)

                } else {
                    recomendacionClima = "Ubicación GPS no disponible en este momento"
                }
            }.addOnFailureListener {
                recomendacionClima = "No se pudo obtener la ubicación en campo"
            }
        } else {
            recomendacionClima = "Permiso de ubicación no concedido (Modo offline)"
        }
    }

    private fun obtenerNombreUbicacion(context: Context, lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(lat, lon, 1) { addresses ->
                        if (addresses.isNotEmpty()) {
                            procesarDireccion(addresses[0])
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(lat, lon, 1)
                    if (!addresses.isNullOrEmpty()) {
                        procesarDireccion(addresses[0])
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun procesarDireccion(address: Address) {
        val ciudad = address.locality ?: address.subAdminArea ?: ""
        val region = address.adminArea ?: ""
        if (ciudad.isNotBlank()) {
            viewModelScope.launch(Dispatchers.Main) {
                ubicacionSugerida = if (region.isNotBlank()) "$ciudad, $region" else ciudad
            }
        }
    }

    private fun consultarApiClima(lat: Double, lon: Double) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val apiKey = "534c472f43ad2d05d17c5fb81b656b29"
                    val respuesta = weatherApi.obtenerClimaActual(lat = lat, lon = lon, apiKey = apiKey)

                    val tempVal = respuesta.main.temp
                    val humVal = respuesta.main.humidity

                    withContext(Dispatchers.Main) {
                        temperaturaClima = "${tempVal.toInt()}°C"
                        humedadClima = "$humVal%"
                        recomendacionClima = evaluarImpactoClima(tempVal, humVal)
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        recomendacionClima = "Sin conexión a internet: Clima no sincronizado"
                    }
                }
            }
        }
    }

    private fun evaluarImpactoClima(temp: Double, humedad: Int): String {
        return when {
            temp > 33.0 -> "⚠️ Alerta: Calor extremo. Desfavorable para liberar agentes biológicos."
            humedad < 55 -> "⚠️ Alerta: Humedad muy baja. Riesgo de desecación de parasitoides."
            temp in 22.0..30.0 && humedad > 65 -> "✅ Clima óptimo: Favorable para la actividad y control del Trichogramma."
            else -> "ℹ️ Condiciones moderadas para el control en campo."
        }
    }

    fun guardarReporte(ubicacionIngresada: String) {
        val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            resultados.forEach { resultado ->
                repository.agregar(
                    Reporte(
                        fecha = fecha,
                        plaga = resultado.label,
                        agenteRecomendado = resultado.agenteRecomendado,
                        ubicacion = ubicacionIngresada.ifBlank { "Sin especificar" },
                        temperatura = temperaturaClima.ifBlank { null },
                        humedad = humedadClima.ifBlank { null },
                        recomendacionClima = recomendacionClima.ifBlank { null }
                    )
                )
            }
        }
    }
}