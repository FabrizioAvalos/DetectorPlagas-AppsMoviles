package com.vallesol.detectorplagas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vallesol.detectorplagas.ui.navigation.AppNavGraph
import com.vallesol.detectorplagas.ui.theme.DetectorPlagasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DetectorPlagasTheme {
                AppNavGraph()
            }
        }
    }
}