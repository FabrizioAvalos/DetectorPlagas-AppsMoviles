package com.vallesol.detectorplagas

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vallesol.detectorplagas.ml.PestDetectorHelper
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PestDetectorTest {

    @Test
    fun elModeloCargaYDetectNoFallaConImagenEnBlanco() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val helper = PestDetectorHelper(ctx)
        try {
            assertTrue("El modelo .tflite no cargó", helper.cargado)

            val bitmap = Bitmap.createBitmap(1200, 900, Bitmap.Config.ARGB_8888)
            val resultados = helper.detect(bitmap)

            // Toda detección devuelta debe superar la confianza mínima (0.25)
            assertTrue(resultados.all { it.confidence >= 0.25f })
        } finally {
            helper.close()
        }
    }
}