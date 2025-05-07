package com.bodega.api.dto

data class CuartelDto(
    val id: Int?,
    val nombre: String,
    val sistema: String,
    val fincaId: Int,
    val encargadoId: Int,
    val variedades: List<VariedadCuartelDto>
)
