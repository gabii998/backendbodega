package com.bodega.api.dto

data class DetalleVariedadDto(
    val idVariedad: Int,
    val nombreVariedad: String,
    val superficie: Double,
    val jornalesTotales: Double,
    val jornalesManuales: Double,
    val jornalesMecanicos: Double,
    val rendimiento: Double,
    val tareasManuales: List<DetalleTareaDto>,
    val tareasMecanicas: List<DetalleTareaDto>
)