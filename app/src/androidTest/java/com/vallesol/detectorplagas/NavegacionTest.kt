package com.vallesol.detectorplagas

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vallesol.detectorplagas.data.local.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavegacionTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    // Deja la tabla de reportes vacía para que la prueba no dependa de datos previos
    @Before
    fun limpiarReportes() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        runBlocking { AppDatabase.getInstance(ctx).reporteDao().borrarTodo() }
    }

    @Test
    fun laBarraInferiorNavegaALaGuiaYMuestraLosDatosDeRoom() {
        rule.onNodeWithText("Guía").performClick()
        rule.onNodeWithText("Guía de Controladores").assertIsDisplayed()

        // Los controladores se precargan en Room de forma asíncrona
        rule.waitUntil(timeoutMillis = 5_000) {
            rule.onAllNodesWithText("Mosca Nativa").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithText("Mosca Nativa").assertIsDisplayed()
    }

    @Test
    fun elBotonCentralAbreDetectar() {
        rule.onNodeWithText("Detectar").performClick()
        rule.onNodeWithText("Analizar Plaga").assertIsDisplayed()
    }

    @Test
    fun reportesSinDatosMuestraElMensajeVacio() {
        rule.onNodeWithText("Reportes").performClick()
        rule.onNodeWithText("No hay reportes guardados aún").assertIsDisplayed()
    }

    @Test
    fun ajustesAbreConfiguracion() {
        rule.onNodeWithText("Ajustes").performClick()
        rule.onNodeWithText("Modo oscuro").assertIsDisplayed()
    }
}