package com.vallesol.detectorplagas.ui.screens.resultado

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vallesol.detectorplagas.Reporte
import com.vallesol.detectorplagas.ReportesRepository
import com.vallesol.detectorplagas.ResultadoTemporal
import com.vallesol.detectorplagas.ml.DetectionResult
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReportesRepository(application)

    val bitmap = ResultadoTemporal.bitmap
    val resultados: List<DetectionResult> = ResultadoTemporal.resultados

    fun guardarReporte(ubicacionIngresada: String) {
        val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            resultados.forEach { resultado ->
                repository.agregar(
                    Reporte(
                        fecha = fecha,
                        plaga = resultado.label,
                        agenteRecomendado = resultado.agenteRecomendado,
                        ubicacion = ubicacionIngresada.ifBlank { "Sin especificar" }
                    )
                )
            }
        }
    }
}
