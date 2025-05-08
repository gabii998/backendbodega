package com.bodega.api.dto

import java.time.LocalDateTime

data class JornalResponse(
    val id: Int,
    val fecha: LocalDateTime,
    val empleadoId: Int,         // Añadido
    val empleadoNombre: String,
    val tareaId: Int,            // Añadido
    val tareaNombre: String,
    val jornales: Double,
    val variedadId: Int?,        // Añadido
    val variedadNombre: String,
    val cuartelId: Int,
)