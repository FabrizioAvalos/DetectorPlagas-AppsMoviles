package com.vallesol.detectorplagas.ui.screens.guiaplagas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vallesol.detectorplagas.data.local.AppDatabase
import com.vallesol.detectorplagas.data.local.ControladorBiologico
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class GuiaPlagasViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).controladorBiologicoDao()

    // Obtenemos los controladores biológicos de Room de forma reactiva
    val controladores: StateFlow<List<ControladorBiologico>> = dao.obtenerTodos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}