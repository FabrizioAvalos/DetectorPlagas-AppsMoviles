package com.vallesol.detectorplagas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.vallesol.detectorplagas.data.PreferenciasRepository
import com.vallesol.detectorplagas.ui.navigation.AppNavGraph
import com.vallesol.detectorplagas.ui.theme.DetectorPlagasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferencias = PreferenciasRepository(applicationContext)

        setContent {
            val modoOscuro by preferencias.modoOscuro.collectAsState(initial = false)
            val tamanoFuente by preferencias.tamanoFuente.collectAsState(initial = "normal")

            DetectorPlagasTheme(
                darkTheme = modoOscuro,
                tamanoFuente = tamanoFuente
            ) {
                AppNavGraph()
            }
        }
    }
}