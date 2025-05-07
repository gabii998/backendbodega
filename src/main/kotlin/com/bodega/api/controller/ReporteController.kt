package com.bodega.api.controller

import com.bodega.api.dto.ReporteCuartelDto
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

    @GetMapping("/anio/{anio}/cuartel/{cuartelId}")
    fun generarReportePorAnioCuartel(
        @PathVariable anio: Int,
        @PathVariable cuartelId: Int
    ): ResponseEntity<ReporteCuartelDto> {
        val reporte = reporteService.generarReportePorAnioCuartel(anio, cuartelId)
        return ResponseEntity.ok(reporte)
    }
}