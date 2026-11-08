package com.vallesol.detectorplagas

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vallesol.detectorplagas.data.local.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReporteDaoTest {

    private lateinit var db: AppDatabase

    @Before
    fun crearBd() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java).build()
    }

    @After
    fun cerrarBd() = db.close()

    private fun reporte(plaga: String) = Reporte(
        fecha = "01/01/2026",
        plaga = plaga,
        agenteRecomendado = "Trichogramma exiguum",
        ubicacion = "Lote 3"
    )

    @Test
    fun insertaLeeYBorraReportes() = runBlocking {
        val dao = db.reporteDao()

        dao.insertar(reporte("Larva_diatraea"))
        assertEquals(1, dao.obtenerTodos().first().size)

        dao.borrarTodo()
        assertTrue(dao.obtenerTodos().first().isEmpty())
    }

    @Test
    fun elReporteMasRecienteApareceCuandoPrimero() = runBlocking {
        val dao = db.reporteDao()

        dao.insertar(reporte("Primero"))
        dao.insertar(reporte("Segundo"))

        // La consulta ordena por id descendente
        assertEquals("Segundo", dao.obtenerTodos().first().first().plaga)
    }
}