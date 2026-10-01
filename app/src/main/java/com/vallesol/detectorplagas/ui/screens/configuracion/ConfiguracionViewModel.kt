package com.vallesol.detectorplagas.ui.screens.configuracion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vallesol.detectorplagas.data.PreferenciasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConfiguracionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PreferenciasRepository(application)

    val modoOscuro: StateFlow<Boolean> = repository.modoOscuro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun setModoOscuro(valor: Boolean) {
        viewModelScope.launch { repository.setModoOscuro(valor) }
    }
}