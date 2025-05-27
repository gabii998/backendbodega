package com.bodega.api.dto

data class CuartelResponse(
    val id: Int,
    val nombre: String,
    val sistema: String,
    val superficieTotal: Double,
    val hileras:Int,
    val encargadoId:Int,
    val encargadoNombre: String,
    val variedades: List<VariedadInfoDto>
)

data class VariedadInfoDto(
    val id: Int,
    val nombre: String,
    val superficie: Double,
    val hileras: Int
)