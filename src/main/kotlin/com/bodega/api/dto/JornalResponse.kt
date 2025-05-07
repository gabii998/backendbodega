package com.bodega.api.dto

import java.time.LocalDateTime

data class JornalResponse(
    val id: Int,
    val fecha: LocalDateTime,
    val empleadoNombre: String,
    val tareaNombre: String,
    val jornales: Double,
    val variedadNombre: String
)