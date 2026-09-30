package com.vallesol.detectorplagas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reportes")
data class Reporte(
    val fecha: String,
    val plaga: String,
    val agenteRecomendado: String,
    val ubicacion: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)