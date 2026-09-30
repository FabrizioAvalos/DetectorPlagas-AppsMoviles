package com.vallesol.detectorplagas.ui.navigation

sealed class Rutas(val ruta: String) {
    object Home : Rutas("home")
    object Detectar : Rutas("detectar")
    object SubirFoto : Rutas("subir_foto")
    object Resultado : Rutas("resultado")
    object GuiaPlagas : Rutas("guia_plagas")
    object Reportes : Rutas("reportes")
    object Configuracion : Rutas("configuracion")
}