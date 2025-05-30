package com.bodega.api.service

import com.bodega.api.Util
import com.bodega.api.dto.*
import com.bodega.api.model.*
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
    //Listado de reportes
    fun generarReportePorAnioCuartel(ano: Int, cuartelId: Int): ReporteDto {
        val cuartel = cuartelRepository.findById(cuartelId)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $cuartelId") }

        val inicioAno = LocalDateTime.of(ano, 1, 1, 0, 0)
        val finAno = LocalDateTime.of(ano, 12, 31, 23, 59, 59)

        val variedadesCuartel = variedadCuartelRepository.findByCuartel(cuartel)

        // Generar reporte por variedad
        val reportesPorVariedad = variedadesCuartel.map { vc ->
            val variedad = vc.variedad ?: throw EntityNotFoundException("Variedad no encontrada")
            val jornalesVariedad = jornalRepository.findByCuartelAndVariedadAndFechaBetween(
                cuartel.id,
                variedad.id, inicioAno, finAno
            )
            val indicadorReporte = getIndicadorVariedad(cuartel, variedad, ano)
            val totalJornalesVariedad = jornalesVariedad.sumOf { it.jornales }
            val rendimientoVariedad = indicadorReporte?.rendimiento?.toDoubleOrNull() ?: 0.00

            ReporteDto(
                id = variedad.id,
                nombre = variedad.nombre,
                superficie = vc.superficie,
                jornales = totalJornalesVariedad,
                rendimiento = rendimientoVariedad,
                esVariedad = true,
                hileras = vc.hileras,
                cuartel = CuartelReporte(
                    id = cuartel.id,
                    nombre = cuartel.nombre,
                ),
                anio = ano.toString()
            )
        }
        return ReporteDto(
            id = cuartel.id,
            nombre = cuartel.nombre,
            superficie = reportesPorVariedad.sumOf { it.superficie },
            jornales = reportesPorVariedad.sumOf { it.jornales },
            hileras = variedadesCuartel.sumOf { it.hileras },
            reporteVariedades = reportesPorVariedad,
            rendimiento = getIndicadorCuartel(cuartel,ano)?.rendimiento?.toDoubleOrNull() ?: 0.00,
            esVariedad = false,
            anio = ano.toString()
        )
    }
    //Detalles
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

        val inicioAno = Util.getInicioAnio(ano)
        val finAno = Util.getFinAno(ano)

        val variedadCuartel = variedadCuartelRepository.findByCuartelAndVariedad(cuartel, variedad)
            .orElseThrow { EntityNotFoundException("No existe relación entre el cuartel $cuartelId y la variedad $variedadId") }

        // Obtener todos los jornales para esta variedad en el período
        val jornalesVariedad = jornalRepository.findByCuartelAndVariedadAndFechaBetween(
            cuartelId,
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
    //Actualizar indicadores
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
    //Obtener indicadores
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
        val indicadorExistente = getIndicadorVariedad(cuartel, variedad, ano)

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
                cuartelId,
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

    fun listarReportesPorAnio(ano: Int,fincaId:Int): List<ReporteDto> {
        return cuartelRepository.findAllByFincaId(fincaId).map { cuartel ->
            generarReportePorAnioCuartel(ano, cuartel.id)
        }
    }

    private fun getIndicadorCuartel(cuartel: Cuartel,ano: Int):IndicadorReporte? {
        return indicadorReporteRepository.findByCuartelAndFechaCargaBetween(
            cuartel,
            LocalDateTime.of(ano, 1, 1, 0, 0),
            LocalDateTime.of(ano, 12, 31, 23, 59, 59)
        ).firstOrNull()
    }

    private fun getIndicadorVariedad(cuartel:Cuartel, variedad:VariedadUva, ano: Int):IndicadorReporte? {
        return indicadorReporteRepository.findByCuartelAndVariedadAndFechaCargaBetween(
            cuartel,
            variedad,
            LocalDateTime.of(ano, 1, 1, 0, 0),
            LocalDateTime.of(ano, 12, 31, 23, 59, 59)
        ).firstOrNull()
    }
}