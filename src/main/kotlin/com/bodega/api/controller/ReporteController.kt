package com.bodega.api.controller

import com.bodega.api.dto.*
import com.bodega.api.service.ReporteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reportes")
class ReporteController(private val reporteService: ReporteService) {

    @GetMapping("/anio/{anio}/finca/{fincaId}")
    fun generarReportePorAnio(@PathVariable anio: Int,@PathVariable fincaId:Int): ResponseEntity<List<ReporteDto>> {
        val reportes = reporteService.listarReportesPorAnio(anio,fincaId)
        return ResponseEntity.ok(reportes)
    }

    @GetMapping("/anio/{anio}/cuartel/{cuartelId}/variedad/{variedadId}/detalle")
    fun obtenerDetalleVariedad(
        @PathVariable anio: Int,
        @PathVariable cuartelId: Int,
        @PathVariable variedadId: Int
    ): ResponseEntity<DetalleVariedadDto> {
        val detalle = reporteService.obtenerDetalleVariedad(anio, cuartelId, variedadId)
        return ResponseEntity.ok(detalle)
    }

    @GetMapping("/anio/{anio}/cuartel/{cuartelId}/detalle")
    fun obtenerDetalleCuartel(
        @PathVariable anio: Int,
        @PathVariable cuartelId: Int
    ): ResponseEntity<DetalleVariedadDto> {
        val detalle = reporteService.obtenerDetalleCuartel(anio, cuartelId)
        return ResponseEntity.ok(detalle)
    }

    @PutMapping("/anio/{anio}/cuartel/{cuartelId}/indicadores")
    fun actualizarIndicadoresCuartel(
        @PathVariable anio: Int,
        @PathVariable cuartelId: Int,
        @RequestBody indicadores: IndicadoresDto
    ): ResponseEntity<IndicadoresDto> {
        val indicadoresActualizados = reporteService.actualizarIndicadoresCuartel(anio, cuartelId, indicadores)
        return ResponseEntity.ok(indicadoresActualizados)
    }

    @PutMapping("/anio/{anio}/cuartel/{cuartelId}/variedad/{variedadId}/indicadores")
    fun actualizarIndicadoresVariedad(
        @PathVariable anio: Int,
        @PathVariable cuartelId: Int,
        @PathVariable variedadId: Int,
        @RequestBody indicadores: IndicadoresDto
    ): ResponseEntity<IndicadoresDto> {
        val indicadoresActualizados = reporteService.actualizarIndicadoresVariedad(anio, cuartelId, variedadId, indicadores)
        return ResponseEntity.ok(indicadoresActualizados)
    }

    @GetMapping("/anio/{anio}/cuartel/{cuartelId}/indicadores")
    fun obtenerIndicadoresCuartel(
        @PathVariable anio: Int,
        @PathVariable cuartelId: Int
    ): ResponseEntity<IndicadoresDto> {
        val indicadores = reporteService.obtenerIndicadoresCuartel(anio, cuartelId)
        return ResponseEntity.ok(indicadores)
    }

    @GetMapping("/anio/{anio}/cuartel/{cuartelId}/variedad/{variedadId}/indicadores")
    fun obtenerIndicadoresVariedad(
        @PathVariable anio: Int,
        @PathVariable cuartelId: Int,
        @PathVariable variedadId: Int
    ): ResponseEntity<IndicadoresDto> {
        val indicadores = reporteService.obtenerIndicadoresVariedad(anio, cuartelId, variedadId)
        return ResponseEntity.ok(indicadores)
    }
}