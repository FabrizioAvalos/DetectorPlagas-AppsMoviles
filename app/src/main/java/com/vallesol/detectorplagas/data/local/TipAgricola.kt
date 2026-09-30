package com.vallesol.detectorplagas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tips_agricolas")
data class TipAgricola(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val titulo: String,
    val descripcion: String,
    val categoria: String
)