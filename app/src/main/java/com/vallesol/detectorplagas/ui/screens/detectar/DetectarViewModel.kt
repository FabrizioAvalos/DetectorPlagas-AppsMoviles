package com.vallesol.detectorplagas.ui.screens.detectar

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.vallesol.detectorplagas.ResultadoTemporal
import com.vallesol.detectorplagas.ml.PestDetectorHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetectarViewModel(application: Application) : AndroidViewModel(application) {

    private val helper = PestDetectorHelper(application)

    // suspend: la inferencia pesada corre en Dispatchers.Default (hilo de fondo),
    // así el hilo principal queda libre para pintar el overlay "Analizando..."
    suspend fun procesarImagen(bitmap: Bitmap) {
        val resultados = withContext(Dispatchers.Default) {
            helper.detect(bitmap)
        }
        ResultadoTemporal.bitmap = bitmap
        ResultadoTemporal.resultados = resultados
    }

    override fun onCleared() {
        super.onCleared()
        helper.close()
    }
}