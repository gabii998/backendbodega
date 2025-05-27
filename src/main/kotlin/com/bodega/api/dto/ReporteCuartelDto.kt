package com.bodega.api.dto

data class ReporteVariedadDto(
    val variedadId: Int,
    val variedadNombre: String,
    val superficie: Double,
    val hileras:Int,
    val jornales: Double,
    val rendimiento: Double
)

data class ReporteDto(
    val id: Int,
    val nombre: String,
    val anio:String,
    val superficie:Double,
    val hileras:Int,
    val jornales:Double,
    val rendimiento:Double,
    val esVariedad:Boolean,
    val cuartel:CuartelReporte? = null,
    val reporteVariedades:List<ReporteDto>? = null
)

data class CuartelReporte(
    val id: Int,
    val nombre: String,
)