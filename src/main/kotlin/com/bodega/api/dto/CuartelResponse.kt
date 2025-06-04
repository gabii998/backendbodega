package com.bodega.api.dto

import com.bodega.api.model.VariedadCuartel

data class CuartelResponse(
    var id: Int = -1,
    var nombre: String = "",
    var sistema: String = "",
    var superficieTotal: Double = 0.0,
    var hileras:Int = 0,
    var encargadoId:Int = -1,
    var encargadoNombre: String = "",
    var variedades: List<VariedadInfoDto> = listOf(),
)

data class VariedadInfoDto(
    val id: Int,
    val idVariedad: Int,
    val nombre: String,
    val superficie: Double,
    val hileras: Int
) {
    constructor(vc:VariedadCuartel):this(
        id = vc.id,
        idVariedad = vc.variedad?.id ?: -1,
        nombre = vc.variedad?.nombre ?: "",
        superficie = vc.superficieActual ?: 0.0,
        hileras = vc.hileras
    )
}