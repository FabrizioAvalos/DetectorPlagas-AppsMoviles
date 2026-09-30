package com.vallesol.detectorplagas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "controladores_biologicos")
data class ControladorBiologico(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombreComun: String,
    val nombreCientifico: String,
    val tipoControl: String,
    val descripcion: String,
    val modoAccion: String,
    val presentacion: String,
    val dosis: String,
    val metodoLiberacion: String,
    val precauciones: String,
    val imagenRes: String
)