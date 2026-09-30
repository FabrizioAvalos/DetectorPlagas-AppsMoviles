package com.vallesol.detectorplagas.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vallesol.detectorplagas.Reporte
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDao {

    @Insert
    suspend fun insertar(reporte: Reporte)

    @Query("SELECT * FROM reportes ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<Reporte>>

    @Query("DELETE FROM reportes")
    suspend fun borrarTodo()
}