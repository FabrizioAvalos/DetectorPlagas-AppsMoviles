package com.vallesol.detectorplagas.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TipAgricolaDao {

    @Query("SELECT * FROM tips_agricolas")
    fun obtenerTodos(): Flow<List<TipAgricola>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(tips: List<TipAgricola>)
}