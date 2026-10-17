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
        val GUARDAR_FOTOS = booleanPreferencesKey("guardar_fotos_automatico")
        val CALIDAD_CAPTURA = stringPreferencesKey("calidad_captura")
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        val TAMANO_FUENTE = stringPreferencesKey("tamano_fuente")
    }

    val guardarFotosAutomatico: Flow<Boolean> = context.dataStore.data
        .map { it[Claves.GUARDAR_FOTOS] ?: true }

    val calidadCaptura: Flow<String> = context.dataStore.data
        .map { it[Claves.CALIDAD_CAPTURA] ?: "media" }

    val modoOscuro: Flow<Boolean> = context.dataStore.data
        .map { it[Claves.MODO_OSCURO] ?: false }

    val tamanoFuente: Flow<String> = context.dataStore.data
        .map { it[Claves.TAMANO_FUENTE] ?: "normal" }

    suspend fun setGuardarFotosAutomatico(valor: Boolean) {
        context.dataStore.edit { it[Claves.GUARDAR_FOTOS] = valor }
    }

    suspend fun setCalidadCaptura(valor: String) {
        context.dataStore.edit { it[Claves.CALIDAD_CAPTURA] = valor }
    }

    suspend fun setModoOscuro(valor: Boolean) {
        context.dataStore.edit { it[Claves.MODO_OSCURO] = valor }
    }

    suspend fun setTamanoFuente(valor: String) {
        context.dataStore.edit { it[Claves.TAMANO_FUENTE] = valor }
    }

    suspend fun borrarHistorialLocal() {
        // TODO: cuando exista el historial real (Room), borrar también esos registros aquí
        context.filesDir.listFiles()?.forEach { it.delete() }
    }
}