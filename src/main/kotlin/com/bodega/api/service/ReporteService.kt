package com.bodega.api.service

import com.bodega.api.dto.*
import com.bodega.api.model.IndicadorReporte
import com.bodega.api.model.TipoTarea
import com.bodega.api.repository.*
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReporteService(
    private val cuartelRepository: CuartelRepository,
    private val jornalRepository: JornalRepository,
    private val variedadCuartelRepository: VariedadCuartelRepository,
    private val variedadRepository:VariedadUvaRepository,
    private val indicadorReporteRepository: IndicadorReporteRepository
) {

    fun generarReportePorAnioCuartelVariedad(ano: Int, cuartelId: Int, variedadId: Int): ReporteVariedadDto {
        // Implementación existente...
        val cuartel = cuartelRepository.findById(cuartelId)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $cuartelId") }

        val variedad = variedadRepository.findById(variedadId)
            .orElseThrow { EntityNotFoundException("Variedad no encontrada con ID: $variedadId") }

        val inicioAno = LocalDateTime.of(ano, 1, 1, 0, 0)
        val finAno = LocalDateTime.of(ano, 12, 31, 23, 59, 59)

        val variedadCuartel = variedadCuartelRepository.findByCuartelAndVariedad(cuartel, variedad)
            .orElseThrow { EntityNotFoundException("No existe relación entre el cuartel $cuartelId y la variedad $variedadId") }

        val jornalesVariedad = jornalRepository.findByCuartelAndVariedadAndFechaBetween(
             variedad.id, inicioAno, finAno
        )

        val totalJornalesVariedad = jornalesVariedad.sumOf { it.jornales }
        val rendimientoVariedad = 0.0

        return ReporteVariedadDto(
            variedadId = variedad.id,
            variedadNombre = variedad.nombre,
            superficie = variedadCuartel.superficie,
            jornales = totalJornalesVariedad,
            rendimiento = rendimientoVariedad,
            hileras = variedadCuartel.hileras,
        )
    }

    fun obtenerDetalleCuartel(ano: Int, cuartelId: Int): DetalleVariedadDto {
        val cuartel = cuartelRepository.findById(cuartelId)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $cuartelId") }

        val variedades = variedadCuartelRepository.findByCuartel(cuartel)

        // Obtener todos los jornales para esta variedad en el período
        val jornalesCuartel = jornalRepository.findAllByCuartelId(cuartel.id.toLong())

        val totalJornalesVariedad = jornalesCuartel.sumOf { it.jornales }

        // Agrupar por tipo de tarea (manual o mecánica)
        val jornalesPorTarea = jornalesCuartel.groupBy { it.tarea }
        // Calcular jornales manuales y mecánicos
        var jornalesManuales = 0.0
        var jornalesMecanicos = 0.0
        val tareasManuales = mutableListOf<DetalleTareaDto>()
        val tareasMecanicas = mutableListOf<DetalleTareaDto>()

        // Agrupar por tareas
        jornalesPorTarea.forEach { (tarea, jornales) ->
            if (tarea == null) return@forEach

            val totalJornalesTarea = jornales.sumOf { it.jornales }

            val detalleTarea = DetalleTareaDto(
                idTarea = tarea.id,
                nombreTarea = tarea.nombre,
                jornales = totalJornalesTarea,
                tipo = tarea.tipo.name
            )

            if (tarea.tipo == TipoTarea.Manual) {
                jornalesManuales += totalJornalesTarea
                tareasManuales.add(detalleTarea)
            } else {
                jornalesMecanicos += totalJornalesTarea
                tareasMecanicas.add(detalleTarea)
            }
        }

        // Calcular rendimiento
        val rendimiento = 0.0

        return DetalleVariedadDto(
            idVariedad = null,
            nombreVariedad = null,
            superficie = variedades.sumOf { it.superficie },
            jornalesTotales = totalJornalesVariedad,
            jornalesManuales = jornalesManuales,
            jornalesMecanicos = jornalesMecanicos,
            rendimiento = rendimiento,
            tareasManuales = tareasManuales,
            tareasMecanicas = tareasMecanicas,
            hileras = variedades.sumOf { it.hileras }
        )
    }

    fun obtenerDetalleVariedad(ano: Int, cuartelId: Int, variedadId: Int): DetalleVariedadDto {
        val cuartel = cuartelRepository.findById(cuartelId)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $cuartelId") }

        val variedad = variedadRepository.findById(variedadId)
            .orElseThrow { EntityNotFoundException("Variedad no encontrada con ID: $variedadId") }

        val inicioAno = LocalDateTime.of(ano, 1, 1, 0, 0)
        val finAno = LocalDateTime.of(ano, 12, 31, 23, 59, 59)

        val variedadCuartel = variedadCuartelRepository.findByCuartelAndVariedad(cuartel, variedad)
            .orElseThrow { EntityNotFoundException("No existe relación entre el cuartel $cuartelId y la variedad $variedadId") }

        // Obtener todos los jornales para esta variedad en el período
        val jornalesVariedad = jornalRepository.findByCuartelAndVariedadAndFechaBetween(
            variedad.id, inicioAno, finAno
        )

        // Cálculo de jornales totales
        val totalJornalesVariedad = jornalesVariedad.sumOf { it.jornales }

        // Agrupar por tipo de tarea (manual o mecánica)
        val jornalesPorTarea = jornalesVariedad.groupBy { it.tarea }

        // Calcular jornales manuales y mecánicos
        var jornalesManuales = 0.0
        var jornalesMecanicos = 0.0

        val tareasManuales = mutableListOf<DetalleTareaDto>()
        val tareasMecanicas = mutableListOf<DetalleTareaDto>()

        // Agrupar por tareas
        jornalesPorTarea.forEach { (tarea, jornales) ->
            if (tarea == null) return@forEach

            val totalJornalesTarea = jornales.sumOf { it.jornales }

            val detalleTarea = DetalleTareaDto(
                idTarea = tarea.id,
                nombreTarea = tarea.nombre,
                jornales = totalJornalesTarea,
                tipo = tarea.tipo.name
            )

            if (tarea.tipo == TipoTarea.Manual) {
                jornalesManuales += totalJornalesTarea
                tareasManuales.add(detalleTarea)
            } else {
                jornalesMecanicos += totalJornalesTarea
                tareasMecanicas.add(detalleTarea)
            }
        }

        // Calcular rendimiento
        val rendimiento = 0.0

        return DetalleVariedadDto(
            idVariedad = variedad.id,
            nombreVariedad = variedad.nombre,
            superficie = variedadCuartel.superficie,
            jornalesTotales = totalJornalesVariedad,
            jornalesManuales = jornalesManuales,
            jornalesMecanicos = jornalesMecanicos,
            rendimiento = rendimiento,
            tareasManuales = tareasManuales,
            tareasMecanicas = tareasMecanicas,
            hileras = variedadCuartel.hileras
        )
    }

    fun generarReportePorAnioCuartel(ano: Int, cuartelId: Int): ReporteCuartelDto {
        val cuartel = cuartelRepository.findById(cuartelId)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $cuartelId") }

        val inicioAno = LocalDateTime.of(ano, 1, 1, 0, 0)
        val finAno = LocalDateTime.of(ano, 12, 31, 23, 59, 59)

        val jornalesCuartel = jornalRepository.findByCuartelAndFechaBetween(cuartelId,inicioAno, finAno)
        val jornalesTotales = jornalesCuartel.sumOf { it.jornales }

        val variedadesCuartel = variedadCuartelRepository.findByCuartel(cuartel)
        val superficieTotal = variedadesCuartel.sumOf { it.superficie }

        // Calcular rendimiento (jornales por hectárea)
        val rendimiento = 0.0

        // Generar reporte por variedad
        val reportesPorVariedad = variedadesCuartel.map { vc ->
            val variedad = vc.variedad ?: throw EntityNotFoundException("Variedad no encontrada")
            val jornalesVariedad = jornalRepository.findByCuartelAndVariedadAndFechaBetween(
                variedad.id, inicioAno, finAno
            )
            val totalJornalesVariedad = jornalesVariedad.sumOf { it.jornales }
            val rendimientoVariedad = if (vc.superficie > 0) totalJornalesVariedad / vc.superficie else 0.0

            ReporteVariedadDto(
                variedadId = variedad.id,
                variedadNombre = variedad.nombre,
                superficie = vc.superficie,
                jornales = totalJornalesVariedad,
                rendimiento = rendimientoVariedad,
                hileras = vc.hileras,
            )
        }

        return ReporteCuartelDto(
            cuartelId = cuartel.id,
            cuartelNombre = cuartel.nombre,
            superficie = superficieTotal,
            fecha = ano.toString(),
            jornalesTotales = jornalesTotales,
            rendimiento = rendimiento,
            variedades = reportesPorVariedad,
            hileras = variedadesCuartel.sumOf { it.hileras },
        )
    }

    fun actualizarIndicadoresCuartel(ano: Int, cuartelId: Int, indicadores: IndicadoresDto): IndicadoresDto {
        val cuartel = cuartelRepository.findById(cuartelId)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $cuartelId") }

        // Buscar el indicador existente o crear uno nuevo
        val indicadorExistente = indicadorReporteRepository.findByCuartelAndFechaCarga(
            cuartel,
            LocalDateTime.of(ano, 1, 1, 0, 0)
        ).firstOrNull()

        val indicadorReporte = indicadorExistente ?: IndicadorReporte(
            cuartel = cuartel,
            fechaCarga = LocalDateTime.of(ano, 1, 1, 0, 0)
        )

        // Actualizar los valores
        indicadorReporte.estructura = indicadores.estructura.toString()
        indicadorReporte.totalProductivo = indicadores.totalProductivo.toString()
        indicadorReporte.jornalesNoProductivos = indicadores.jornalesNoProductivos.toString()
        indicadorReporte.jornalesPagados = indicadores.jornalesPagados.toString()
        indicadorReporte.rendimiento = indicadores.rendimiento.toString()
        // Nota: Si necesitas almacenar quintalPorJornal, podrías agregarlo al modelo IndicadorReporte

        // Guardar los cambios
        val indicadorGuardado = indicadorReporteRepository.save(indicadorReporte)

        // Retornar el DTO actualizado
        return IndicadoresDto(
            estructura = indicadorGuardado.estructura.toDoubleOrNull() ?: 0.0,
            totalProductivo = indicadorGuardado.totalProductivo.toDoubleOrNull() ?: 0.0,
            jornalesNoProductivos = indicadorGuardado.jornalesNoProductivos.toDoubleOrNull() ?: 0.0,
            jornalesPagados = indicadorGuardado.jornalesPagados.toDoubleOrNull() ?: 0.0,
            rendimiento = indicadorGuardado.rendimiento.toDoubleOrNull() ?: 0.0,
            quintalPorJornal = indicadores.quintalPorJornal // Mantener el valor recibido
        )
    }

    fun actualizarIndicadoresVariedad(ano: Int, cuartelId: Int, variedadId: Int, indicadores: IndicadoresDto): IndicadoresDto {
        val cuartel = cuartelRepository.findById(cuartelId)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $cuartelId") }

        val variedad = variedadRepository.findById(variedadId)
            .orElseThrow { EntityNotFoundException("Variedad no encontrada con ID: $variedadId") }

        // Verificar que la variedad pertenezca al cuartel
//        val variedadCuartel = variedadCuartelRepository.findByCuartelAndVariedad(cuartel, variedad)
//            .orElseThrow { EntityNotFoundException("La variedad no está asociada con este cuartel") }

        // Buscar el indicador existente o crear uno nuevo
        val indicadorExistente = indicadorReporteRepository.findByCuartelAndVariedadAndFechaCargaBetween(
            cuartel,
            variedad,
            LocalDateTime.of(ano, 1, 1, 0, 0),
            LocalDateTime.of(ano, 12, 31, 23, 59, 59)
        ).firstOrNull()

        val indicadorReporte = indicadorExistente ?: IndicadorReporte(
            cuartel = cuartel,
            variedad = variedad,
            fechaCarga = LocalDateTime.of(ano, 1, 1, 0, 0)
        )

        // Actualizar los valores
        indicadorReporte.estructura = indicadores.estructura.toString()
        indicadorReporte.totalProductivo = indicadores.totalProductivo.toString()
        indicadorReporte.jornalesNoProductivos = indicadores.jornalesNoProductivos.toString()
        indicadorReporte.jornalesPagados = indicadores.jornalesPagados.toString()
        indicadorReporte.rendimiento = indicadores.rendimiento.toString()

        // Guardar los cambios
        val indicadorGuardado = indicadorReporteRepository.save(indicadorReporte)

        // Retornar el DTO actualizado
        return IndicadoresDto(
            estructura = indicadorGuardado.estructura.toDoubleOrNull() ?: 0.0,
            totalProductivo = indicadorGuardado.totalProductivo.toDoubleOrNull() ?: 0.0,
            jornalesNoProductivos = indicadorGuardado.jornalesNoProductivos.toDoubleOrNull() ?: 0.0,
            jornalesPagados = indicadorGuardado.jornalesPagados.toDoubleOrNull() ?: 0.0,
            rendimiento = indicadorGuardado.rendimiento.toDoubleOrNull() ?: 0.0,
            quintalPorJornal = indicadores.quintalPorJornal
        )
    }

    fun obtenerIndicadoresCuartel(ano: Int, cuartelId: Int): IndicadoresDto {
        val cuartel = cuartelRepository.findById(cuartelId)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $cuartelId") }

        // Buscar indicadores existentes
        val indicadorExistente = indicadorReporteRepository.findByCuartelAndFechaCarga(
            cuartel,
            LocalDateTime.of(ano, 1, 1, 0, 0)
        ).firstOrNull()

        return if (indicadorExistente != null) {
            val jornalesCuartel = jornalRepository.findByCuartelAndFechaBetween(
                cuartelId,
                LocalDateTime.of(ano, 1, 1, 0, 0),
                LocalDateTime.of(ano, 12, 31, 23, 59, 59)
            )
            val totalJornales = jornalesCuartel.sumOf { it.jornales }
            val rendimiento = indicadorExistente.rendimiento.toDoubleOrNull() ?: 0.0
            val superfice = cuartel.variedades.sumOf { it.superficie }
            // Retornar indicadores existentes
            IndicadoresDto(
                estructura = indicadorExistente.estructura.toDoubleOrNull() ?: 0.0,
                totalProductivo = indicadorExistente.totalProductivo.toDoubleOrNull() ?: 0.0,
                jornalesNoProductivos = indicadorExistente.jornalesNoProductivos.toDoubleOrNull() ?: 0.0,
                jornalesPagados = indicadorExistente.jornalesPagados.toDoubleOrNull() ?: 0.0,
                rendimiento = rendimiento,
                quintalPorJornal = if (totalJornales > 0) rendimiento / (totalJornales / superfice) else 0.0
            )
        } else {
            // Si no existen, calcular valores por defecto
            val jornalesCuartel = jornalRepository.findByCuartelAndFechaBetween(
                cuartelId,
                LocalDateTime.of(ano, 1, 1, 0, 0),
                LocalDateTime.of(ano, 12, 31, 23, 59, 59)
            )
            val totalJornales = jornalesCuartel.sumOf { it.jornales }
            val rendimiento = 0.0

            IndicadoresDto(
                estructura = 0.0,
                totalProductivo = totalJornales,
                jornalesNoProductivos = 0.0,
                jornalesPagados = 0.0,
                rendimiento = rendimiento,
                quintalPorJornal = 0.0
            )
        }
    }

    fun obtenerIndicadoresVariedad(ano: Int, cuartelId: Int, variedadId: Int): IndicadoresDto {
        val cuartel = cuartelRepository.findById(cuartelId)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $cuartelId") }

        val variedad = variedadRepository.findById(variedadId)
            .orElseThrow { EntityNotFoundException("Variedad no encontrada con ID: $variedadId") }

        // Buscar indicadores existentes
        val indicadorExistente = indicadorReporteRepository.findByCuartelAndVariedadAndFechaCargaBetween(
            cuartel,
            variedad,
            LocalDateTime.of(ano, 1, 1, 0, 0),
            LocalDateTime.of(ano, 12, 31, 23, 59, 59)
        ).firstOrNull()

        return if (indicadorExistente != null) {
            // Retornar indicadores existentes
            IndicadoresDto(
                estructura = indicadorExistente.estructura.toDoubleOrNull() ?: 0.0,
                totalProductivo = indicadorExistente.totalProductivo.toDoubleOrNull() ?: 0.0,
                jornalesNoProductivos = indicadorExistente.jornalesNoProductivos.toDoubleOrNull() ?: 0.0,
                jornalesPagados = indicadorExistente.jornalesPagados.toDoubleOrNull() ?: 0.0,
                rendimiento = indicadorExistente.rendimiento.toDoubleOrNull() ?: 0.0,
                quintalPorJornal = 0.0
            )
        } else {
            // Si no existen, calcular valores por defecto
            val variedadCuartel = variedadCuartelRepository.findByCuartelAndVariedad(cuartel, variedad)
                .orElseThrow { EntityNotFoundException("La variedad no está asociada con este cuartel") }

            val jornalesVariedad = jornalRepository.findByCuartelAndVariedadAndFechaBetween(
                variedad.id,
                LocalDateTime.of(ano, 1, 1, 0, 0),
                LocalDateTime.of(ano, 12, 31, 23, 59, 59)
            )

            val totalJornales = jornalesVariedad.sumOf { it.jornales }
            val superficie = variedadCuartel.superficie
            val rendimiento = if (superficie > 0) totalJornales / superficie else 0.0

            // Calcular indicadores por defecto
            val estructura = totalJornales * 0.02
            val jornalesNoProductivos = totalJornales * 0.06
            val jornalesPagados = totalJornales + jornalesNoProductivos + estructura

            IndicadoresDto(
                estructura = estructura,
                totalProductivo = totalJornales,
                jornalesNoProductivos = jornalesNoProductivos,
                jornalesPagados = jornalesPagados,
                rendimiento = rendimiento,
                quintalPorJornal = if (totalJornales > 0) rendimiento / (totalJornales / superficie) else 0.0
            )
        }
    }

    fun listarReportesPorAnio(ano: Int,fincaId:Int): List<ReporteCuartelDto> {
        return cuartelRepository.findAllByFincaId(fincaId).map { cuartel ->
            generarReportePorAnioCuartel(ano, cuartel.id)
        }
    }
}