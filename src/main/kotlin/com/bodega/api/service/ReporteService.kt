package com.bodega.api.service

import com.bodega.api.dto.ReporteCuartelDto
import com.bodega.api.dto.ReporteVariedadDto
import com.bodega.api.repository.CuartelRepository
import com.bodega.api.repository.JornalRepository
import com.bodega.api.repository.VariedadCuartelRepository
import com.bodega.api.repository.VariedadUvaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReporteService(
    private val cuartelRepository: CuartelRepository,
    private val jornalRepository: JornalRepository,
    private val variedadCuartelRepository: VariedadCuartelRepository,
    private val variedadRepository:VariedadUvaRepository
) {

    fun generarReportePorAnioCuartelVariedad(ano: Int, cuartelId: Int, variedadId: Int): ReporteVariedadDto {
        val cuartel = cuartelRepository.findById(cuartelId)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $cuartelId") }

        val variedad = variedadRepository.findById(variedadId)
            .orElseThrow { EntityNotFoundException("Variedad no encontrada con ID: $variedadId") }

        val inicioAno = LocalDateTime.of(ano, 1, 1, 0, 0)
        val finAno = LocalDateTime.of(ano, 12, 31, 23, 59, 59)

        val variedadCuartel = variedadCuartelRepository.findByCuartelAndVariedad(cuartel, variedad)
            .orElseThrow { EntityNotFoundException("No existe relación entre el cuartel $cuartelId y la variedad $variedadId") }

        val jornalesVariedad = jornalRepository.findByCuartelAndVariedadAndFechaBetween(
            cuartel, variedad, inicioAno, finAno
        )

        val totalJornalesVariedad = jornalesVariedad.sumOf { it.jornales }
        val rendimientoVariedad = if (variedadCuartel.superficie > 0) totalJornalesVariedad / variedadCuartel.superficie else 0.0

        return ReporteVariedadDto(
            variedadId = variedad.id,
            variedadNombre = variedad.nombre,
            superficie = variedadCuartel.superficie,
            jornales = totalJornalesVariedad,
            rendimiento = rendimientoVariedad
        )
    }

    fun generarReportePorAnioCuartel(ano: Int, cuartelId: Int): ReporteCuartelDto {
        val cuartel = cuartelRepository.findById(cuartelId)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $cuartelId") }

        val inicioAno = LocalDateTime.of(ano, 1, 1, 0, 0)
        val finAno = LocalDateTime.of(ano, 12, 31, 23, 59, 59)

        val jornalesCuartel = jornalRepository.findByCuartelAndFechaBetween(cuartel, inicioAno, finAno)
        val jornalesTotales = jornalesCuartel.sumOf { it.jornales }

        val variedadesCuartel = variedadCuartelRepository.findByCuartel(cuartel)
        val superficieTotal = variedadesCuartel.sumOf { it.superficie }

        // Calcular rendimiento (jornales por hectárea)
        val rendimiento = if (superficieTotal > 0) jornalesTotales / superficieTotal else 0.0

        // Generar reporte por variedad
        val reportesPorVariedad = variedadesCuartel.map { vc ->
            val variedad = vc.variedad ?: throw EntityNotFoundException("Variedad no encontrada")
            val jornalesVariedad = jornalRepository.findByCuartelAndVariedadAndFechaBetween(
                cuartel, variedad, inicioAno, finAno
            )
            val totalJornalesVariedad = jornalesVariedad.sumOf { it.jornales }
            val rendimientoVariedad = if (vc.superficie > 0) totalJornalesVariedad / vc.superficie else 0.0

            ReporteVariedadDto(
                variedadId = variedad.id,
                variedadNombre = variedad.nombre,
                superficie = vc.superficie,
                jornales = totalJornalesVariedad,
                rendimiento = rendimientoVariedad
            )
        }

        return ReporteCuartelDto(
            cuartelId = cuartel.id,
            cuartelNombre = cuartel.nombre,
            superficie = superficieTotal,
            fecha = ano.toString(),
            jornalesTotales = jornalesTotales,
            rendimiento = rendimiento,
            variedades = reportesPorVariedad
        )
    }

    fun listarReportesPorAnio(ano: Int): List<ReporteCuartelDto> {
        return cuartelRepository.findAll().map { cuartel ->
            generarReportePorAnioCuartel(ano, cuartel.id)
        }
    }
}