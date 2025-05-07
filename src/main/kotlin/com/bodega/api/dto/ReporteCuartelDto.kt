package com.bodega.api.dto

data class ReporteCuartelDto(
    val cuartelId: Int,
    val cuartelNombre: String,
    val superficie: Double,
    val fecha: String,
    val jornalesTotales: Double,
    val rendimiento: Double,
    val variedades: List<ReporteVariedadDto>
)

data class ReporteVariedadDto(
    val variedadId: Int,
    val variedadNombre: String,
    val superficie: Double,
    val jornales: Double,
    val rendimiento: Double
)