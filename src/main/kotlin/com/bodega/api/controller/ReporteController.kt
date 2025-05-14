package com.bodega.api.controller

import com.bodega.api.dto.DetalleVariedadDto
import com.bodega.api.dto.IndicadoresDto
import com.bodega.api.dto.ReporteCuartelDto
import com.bodega.api.dto.ReporteVariedadDto
import com.bodega.api.service.ReporteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reportes")
class ReporteController(private val reporteService: ReporteService) {

    @GetMapping("/anio/{anio}/finca/{fincaId}")
    fun generarReportePorAnio(@PathVariable anio: Int,@PathVariable fincaId:Int): ResponseEntity<List<ReporteCuartelDto>> {
        val reportes = reporteService.listarReportesPorAnio(anio,fincaId)
        return ResponseEntity.ok(reportes)
    }

    @GetMapping("/anio/{anio}/cuartel/{cuartelId}/variedad/{variedadId}")
    fun generarReportePorAnioCuartelVariedad(
        @PathVariable anio: Int,
        @PathVariable cuartelId: Int,
        @PathVariable variedadId: Int
    ): ResponseEntity<ReporteVariedadDto> {
        val reporte = reporteService.generarReportePorAnioCuartelVariedad(anio, cuartelId, variedadId)
        return ResponseEntity.ok(reporte)
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