package com.bodega.api.dto

import java.time.LocalDateTime

data class JornalDto(
    val id: Int?,
    val fecha: LocalDateTime,
    val jornales: Double,
    val empleadoId: Int,
    val tareaId: Int,
    val variedadId: Int
)