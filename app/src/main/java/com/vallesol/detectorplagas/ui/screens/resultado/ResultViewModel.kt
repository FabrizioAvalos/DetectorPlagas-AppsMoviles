package com.vallesol.detectorplagas.ui.screens.resultado

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.vallesol.detectorplagas.ResultadoTemporal
import com.vallesol.detectorplagas.ml.DetectionResult

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    val bitmap = ResultadoTemporal.bitmap
    val resultados: List<DetectionResult> = ResultadoTemporal.resultados
}
