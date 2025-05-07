package com.bodega.api.controller

import com.bodega.api.dto.ReporteCuartelDto
import com.bodega.api.dto.ReporteVariedadDto
import com.bodega.api.service.ReporteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reportes")
class ReporteController(private val reporteService: ReporteService) {

    @GetMapping("/anio/{anio}")
    fun generarReportePorAnio(@PathVariable anio: Int): ResponseEntity<List<ReporteCuartelDto>> {
        val reportes = reporteService.listarReportesPorAnio(anio)
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
}