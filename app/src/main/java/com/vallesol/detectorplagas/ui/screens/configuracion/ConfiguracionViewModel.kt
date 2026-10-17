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

    val guardarFotosAutomatico: StateFlow<Boolean> = repository.guardarFotosAutomatico
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val calidadCaptura: StateFlow<String> = repository.calidadCaptura
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "media")

    val modoOscuro: StateFlow<Boolean> = repository.modoOscuro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val tamanoFuente: StateFlow<String> = repository.tamanoFuente
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "normal")

    fun setGuardarFotosAutomatico(valor: Boolean) {
        viewModelScope.launch { repository.setGuardarFotosAutomatico(valor) }
    }

    fun setCalidadCaptura(valor: String) {
        viewModelScope.launch { repository.setCalidadCaptura(valor) }
    }

    fun setModoOscuro(valor: Boolean) {
        viewModelScope.launch { repository.setModoOscuro(valor) }
    }

    fun setTamanoFuente(valor: String) {
        viewModelScope.launch { repository.setTamanoFuente(valor) }
    }

    fun borrarHistorial() {
        viewModelScope.launch { repository.borrarHistorialLocal() }
    }
}