package com.vallesol.detectorplagas.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vallesol.detectorplagas.data.local.AppDatabase
import com.vallesol.detectorplagas.data.local.TipAgricola
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).tipAgricolaDao()

    val tips: StateFlow<List<TipAgricola>> = dao.obtenerTodos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}