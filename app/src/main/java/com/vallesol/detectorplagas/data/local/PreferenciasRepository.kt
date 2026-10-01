package com.vallesol.detectorplagas.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "configuracion")

class PreferenciasRepository(private val context: Context) {

    private object Claves {
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data
        .map { it[Claves.MODO_OSCURO] ?: false }

    suspend fun setModoOscuro(valor: Boolean) {
        context.dataStore.edit { it[Claves.MODO_OSCURO] = valor }
    }
}