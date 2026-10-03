package com.vallesol.detectorplagas.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.max
import kotlin.math.min

private data class InfoClase(val nombreCientifico: String, val agenteRecomendado: String)

private val INFO_POR_CLASE = mapOf(
    "Tallo_danado" to InfoClase("Diatraea saccharalis", "Trichogramma exiguum"),
    "Hoja_danada" to InfoClase("Diatraea saccharalis", "Trichogramma exiguum"),
    "Hueco_diatraea_tallo" to InfoClase("Diatraea saccharalis", "Billaea claripalpis"),
    "Cana_con_diatraea" to InfoClase("Diatraea saccharalis", "Billaea claripalpis"),
    "Larva_diatraea" to InfoClase("Diatraea saccharalis", "Trichogramma exiguum"),
)

private const val NOMBRE_MODELO = "plagas_model.tflite"
private const val NOMBRE_LABELS = "clases.txt"
private const val CONFIANZA_MINIMA = 0.25f
private const val IOU_NMS = 0.45f

class PestDetectorHelper(private val context: Context) {

    private var interpreter: Interpreter? = null
    private var labels: List<String> = emptyList()
    private var inputSize: Int = 480

    init {
        try {
            interpreter = Interpreter(cargarModelo(), Interpreter.Options().apply { setNumThreads(4) })
            labels = cargarLabels()
            interpreter?.let {
                val forma = it.getInputTensor(0).shape()
                inputSize = forma[1]
            }
            android.util.Log.d("PestDetector", "Modelo cargado OK. inputSize=$inputSize labels=$labels")
        } catch (e: Exception) {
            android.util.Log.e("PestDetector", "ERROR cargando el modelo: ${e.message}", e)
        }
    }

    fun detect(bitmap: Bitmap): List<DetectionResult> {
        return try {
            detectInterno(bitmap)
        } catch (e: Exception) {
            android.util.Log.e("PestDetector", "ERROR inesperado en detect(): ${e.message}", e)
            emptyList()
        }
    }

    private fun detectInterno(bitmap: Bitmap): List<DetectionResult> {
        val modelo = interpreter ?: run {
            android.util.Log.e("PestDetector", "El interpreter es null, el modelo no llegó a cargar.")
            return emptyList()
        }

        val (bitmapEntrada, escala, padX, padY) = letterbox(bitmap, inputSize)
        val entrada = bitmapABuffer(bitmapEntrada)

        val formaSalida = modelo.getOutputTensor(0).shape()
        if (formaSalida.size != 3) {
            android.util.Log.e("PestDetector", "La salida no tiene 3 dimensiones, no puedo decodificarla así.")
            return emptyList()
        }

        val numCajas = formaSalida[2]
        val numValores = formaSalida[1]
        val salida = Array(1) { Array(numValores) { FloatArray(numCajas) } }

        try {
            modelo.run(entrada, salida)
        } catch (e: Exception) {
            android.util.Log.e("PestDetector", "ERROR al correr el modelo (modelo.run): ${e.message}", e)
            return emptyList()
        }

        val candidatos = mutableListOf<Deteccion>()
        val numClases = numValores - 4

        for (i in 0 until numCajas) {
            var mejorClase = -1
            var mejorScore = 0f
            for (c in 0 until numClases) {
                val score = salida[0][4 + c][i]
                if (score > mejorScore) {
                    mejorScore = score
                    mejorClase = c
                }
            }
            if (mejorScore < CONFIANZA_MINIMA || mejorClase == -1) continue

            val xc = salida[0][0][i]
            val yc = salida[0][1][i]
            val w = salida[0][2][i]
            val h = salida[0][3][i]

            candidatos.add(
                Deteccion(
                    claseId = mejorClase,
                    score = mejorScore,
                    x1 = xc - w / 2f,
                    y1 = yc - h / 2f,
                    x2 = xc + w / 2f,
                    y2 = yc + h / 2f,
                )
            )
        }

        val seleccionadas = nms(candidatos, IOU_NMS)

        return seleccionadas.map { d ->
            val nombreClase = labels.getOrElse(d.claseId) { "Clase ${d.claseId}" }
            val info = INFO_POR_CLASE[nombreClase]
            val cajaOriginal = deshacerLetterbox(d.x1, d.y1, d.x2, d.y2, escala, padX, padY, bitmap.width, bitmap.height)

            DetectionResult(
                label = nombreClase,
                nombreCientifico = info?.nombreCientifico ?: "",
                confidence = d.score,
                agenteRecomendado = info?.agenteRecomendado ?: "",
                boundingBox = cajaOriginal,
            )
        }
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }

    private fun cargarModelo(): MappedByteBuffer {
        val descriptor = context.assets.openFd(NOMBRE_MODELO)
        FileInputStream(descriptor.fileDescriptor).use { flujo ->
            return flujo.channel.map(
                FileChannel.MapMode.READ_ONLY,
                descriptor.startOffset,
                descriptor.declaredLength,
            )
        }
    }

    private fun cargarLabels(): List<String> {
        return context.assets.open(NOMBRE_LABELS).bufferedReader().useLines { it.toList() }
            .map { it.trim() }.filter { it.isNotEmpty() }
    }

    private data class Letterbox(val bitmap: Bitmap, val escala: Float, val padX: Float, val padY: Float)

    private fun letterbox(origen: Bitmap, tamano: Int): Letterbox {
        val escala = min(tamano.toFloat() / origen.width, tamano.toFloat() / origen.height)
        val nuevoAncho = (origen.width * escala).toInt()
        val nuevoAlto = (origen.height * escala).toInt()
        val redimensionado = Bitmap.createScaledBitmap(origen, nuevoAncho, nuevoAlto, true)

        val salida = Bitmap.createBitmap(tamano, tamano, Bitmap.Config.ARGB_8888)
        val padX = (tamano - nuevoAncho) / 2f
        val padY = (tamano - nuevoAlto) / 2f
        val canvas = android.graphics.Canvas(salida)
        canvas.drawColor(android.graphics.Color.rgb(114, 114, 114))
        canvas.drawBitmap(redimensionado, padX, padY, null)

        return Letterbox(salida, escala, padX, padY)
    }

    private fun deshacerLetterbox(
        x1: Float, y1: Float, x2: Float, y2: Float,
        escala: Float, padX: Float, padY: Float,
        anchoOriginal: Int, altoOriginal: Int,
    ): RectF {
        val px1 = x1 * inputSize
        val py1 = y1 * inputSize
        val px2 = x2 * inputSize
        val py2 = y2 * inputSize

        val ox1 = ((px1 - padX) / escala).coerceIn(0f, anchoOriginal.toFloat())
        val oy1 = ((py1 - padY) / escala).coerceIn(0f, altoOriginal.toFloat())
        val ox2 = ((px2 - padX) / escala).coerceIn(0f, anchoOriginal.toFloat())
        val oy2 = ((py2 - padY) / escala).coerceIn(0f, altoOriginal.toFloat())

        return RectF(ox1 / anchoOriginal, oy1 / altoOriginal, ox2 / anchoOriginal, oy2 / altoOriginal)
    }

    private fun bitmapABuffer(bitmap: Bitmap): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
        buffer.order(ByteOrder.nativeOrder())
        val pixeles = IntArray(inputSize * inputSize)
        bitmap.getPixels(pixeles, 0, inputSize, 0, 0, inputSize, inputSize)

        for (pixel in pixeles) {
            buffer.putFloat(((pixel shr 16) and 0xFF) / 255f)
            buffer.putFloat(((pixel shr 8) and 0xFF) / 255f)
            buffer.putFloat((pixel and 0xFF) / 255f)
        }
        buffer.rewind()
        return buffer
    }

    private data class Deteccion(val claseId: Int, val score: Float, val x1: Float, val y1: Float, val x2: Float, val y2: Float)

    private fun nms(cajas: List<Deteccion>, umbralIou: Float): List<Deteccion> {
        val resultado = mutableListOf<Deteccion>()
        for (claseId in cajas.map { it.claseId }.distinct()) {
            val delaClase = cajas.filter { it.claseId == claseId }.sortedByDescending { it.score }.toMutableList()
            while (delaClase.isNotEmpty()) {
                val mejor = delaClase.removeAt(0)
                resultado.add(mejor)
                delaClase.removeAll { iou(mejor, it) > umbralIou }
            }
        }
        return resultado
    }

    private fun iou(a: Deteccion, b: Deteccion): Float {
        val xA = max(a.x1, b.x1)
        val yA = max(a.y1, b.y1)
        val xB = min(a.x2, b.x2)
        val yB = min(a.y2, b.y2)

        val interseccion = max(0f, xB - xA) * max(0f, yB - yA)
        val areaA = (a.x2 - a.x1) * (a.y2 - a.y1)
        val areaB = (b.x2 - b.x1) * (b.y2 - b.y1)
        val union = areaA + areaB - interseccion

        return if (union <= 0f) 0f else interseccion / union
    }
}