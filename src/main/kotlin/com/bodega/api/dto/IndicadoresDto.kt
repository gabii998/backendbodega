package com.bodega.api.dto

import com.bodega.api.model.IndicadorReporte

data class IndicadoresDto(
    val estructura: Double,
    val totalProductivo: Double,
    val jornalesNoProductivos: Double,
    val jornalesPagados: Double,
    val rendimiento: Double,
    val quintalPorJornal: Double
) {
    constructor(i:IndicadorReporte):this(
        estructura = i.estructura.toDouble(),
        totalProductivo = i.totalProductivo.toDouble(),
        jornalesNoProductivos = i.jornalesNoProductivos.toDouble(),
        jornalesPagados = i.jornalesPagados.toDouble(),
        rendimiento = i.rendimiento.toDouble(),
        quintalPorJornal = i.quintalPorJornal.toDoubleOrNull() ?: 0.00
    )
}