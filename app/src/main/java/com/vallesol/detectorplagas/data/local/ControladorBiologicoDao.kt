package com.vallesol.detectorplagas.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ControladorBiologicoDao {

    @Query("SELECT * FROM controladores_biologicos")
    fun obtenerTodos(): Flow<List<ControladorBiologico>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(controladores: List<ControladorBiologico>)
}