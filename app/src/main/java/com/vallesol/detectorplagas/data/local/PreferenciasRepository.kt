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
        val TAMANO_FUENTE = stringPreferencesKey("tamano_fuente")
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data
        .map { it[Claves.MODO_OSCURO] ?: false }

    val tamanoFuente: Flow<String> = context.dataStore.data
        .map { it[Claves.TAMANO_FUENTE] ?: "normal" }

    suspend fun setModoOscuro(valor: Boolean) {
        context.dataStore.edit { it[Claves.MODO_OSCURO] = valor }
    }

    suspend fun setTamanoFuente(valor: String) {
        context.dataStore.edit { it[Claves.TAMANO_FUENTE] = valor }
    }
}