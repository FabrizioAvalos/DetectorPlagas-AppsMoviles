package com.vallesol.detectorplagas

import android.content.Context
import com.vallesol.detectorplagas.data.local.AppDatabase
import kotlinx.coroutines.flow.Flow

class ReportesRepository(context: Context) {

    private val dao = AppDatabase.getInstance(context).reporteDao()

    val reportes: Flow<List<Reporte>> = dao.obtenerTodos()

    suspend fun agregar(reporte: Reporte) {
        dao.insertar(reporte)
    }

    suspend fun borrarTodo() {
        dao.borrarTodo()
    }
}
