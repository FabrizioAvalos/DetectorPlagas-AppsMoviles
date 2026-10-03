package com.vallesol.detectorplagas.ml

import android.graphics.RectF

/**
 * boundingBox usa valores normalizados (0f a 1f), relativos al ancho/alto
 * de la imagen, no píxeles absolutos — así el overlay puede dibujarlo sin
 * importar la resolución real de la foto xd WAZAAA
 */
data class DetectionResult(
    val label: String,
    val nombreCientifico: String,
    val confidence: Float,
    val agenteRecomendado: String,
    val boundingBox: RectF
)