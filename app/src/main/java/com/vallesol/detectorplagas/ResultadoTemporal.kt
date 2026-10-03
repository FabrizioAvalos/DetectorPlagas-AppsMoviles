package com.vallesol.detectorplagas

import android.graphics.Bitmap
import com.vallesol.detectorplagas.ml.DetectionResult

object ResultadoTemporal {
    var bitmap: Bitmap? = null
    var resultados: List<DetectionResult> = emptyList()
}