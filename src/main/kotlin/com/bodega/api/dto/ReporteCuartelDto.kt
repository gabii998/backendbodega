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
    val id: Int? = null,
    val nombre: String? = null,
    val anio:String? = null,
    val superficie:Double? = null,
    val hileras:Int? = null,
    val jornales:Double? = null,
    val rendimiento:Double? = null,
    val tipoReporte: TipoReporte? = null,
    val cuartel:CuartelReporte? = null,
    val reporteVariedades:List<ReporteDto>? = null
)

enum class TipoReporte {
    VARIEDAD,CUARTEL,ESPALDERO,PARRAL,GENERAL
}

data class CuartelReporte(
    val id: Int,
    val nombre: String,
)