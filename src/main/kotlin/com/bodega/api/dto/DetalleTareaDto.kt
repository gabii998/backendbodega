package com.bodega.api.dto

data class DetalleTareaDto(
    val idTarea: Int,
    val nombreTarea: String,
    val jornales: Double,
    val tipo: String
)
