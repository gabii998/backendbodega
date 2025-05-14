package com.bodega.api.dto

data class IndicadoresDto(
    val estructura: Double,
    val totalProductivo: Double,
    val jornalesNoProductivos: Double,
    val jornalesPagados: Double,
    val rendimiento: Double,
    val quintalPorJornal: Double
)