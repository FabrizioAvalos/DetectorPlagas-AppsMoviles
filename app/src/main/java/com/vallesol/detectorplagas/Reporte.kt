package com.vallesol.detectorplagas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reportes")
data class Reporte(
    val fecha: String,
    val plaga: String,
    val agenteRecomendado: String,
    val ubicacion: String,
    // 🌤️ Nuevos campos climáticos opcionales (nullable por defecto para modo offline)
    val temperatura: String? = null,
    val humedad: String? = null,
    val recomendacionClima: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)