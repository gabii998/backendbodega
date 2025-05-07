package com.bodega.api.service

import com.bodega.api.dto.JornalDto
import com.bodega.api.dto.JornalResponse
import com.bodega.api.model.Jornal
import com.bodega.api.repository.EmpleadoRepository
import com.bodega.api.repository.JornalRepository
import com.bodega.api.repository.TareaRepository
import com.bodega.api.repository.VariedadUvaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class JornalService(
    private val jornalRepository: JornalRepository,
    private val empleadoRepository: EmpleadoRepository,
    private val tareaRepository: TareaRepository,
    private val variedadRepository: VariedadUvaRepository
) {

    @Transactional
    fun crearJornal(jornalDto: JornalDto, usuarioId: Int): JornalResponse {
        val empleado = empleadoRepository.findById(jornalDto.empleadoId)
            .orElseThrow { EntityNotFoundException("Empleado no encontrado con ID: ${jornalDto.empleadoId}") }

        val tarea = tareaRepository.findById(jornalDto.tareaId)
            .orElseThrow { EntityNotFoundException("Tarea no encontrada con ID: ${jornalDto.tareaId}") }

        val variedad = variedadRepository.findById(jornalDto.variedadId)
            .orElseThrow { EntityNotFoundException("Variedad no encontrada con ID: ${jornalDto.variedadId}") }

        val usuario = empleadoRepository.findById(usuarioId)
            .orElseThrow { EntityNotFoundException("Usuario no encontrado con ID: $usuarioId") }

        val jornal = Jornal(
            empleado = empleado,
            tarea = tarea,
            jornales = jornalDto.jornales,
            fecha = jornalDto.fecha,
            cargadoPor = usuario,
            variedad = variedad
        )

        val savedJornal = jornalRepository.save(jornal)

        return JornalResponse(
            id = savedJornal.id,
            fecha = savedJornal.fecha,
            empleadoNombre = savedJornal.empleado?.nombre ?: "",
            tareaNombre = savedJornal.tarea?.nombre ?: "",
            jornales = savedJornal.jornales,
            variedadNombre = savedJornal.variedad?.nombre ?: ""
        )
    }

    @Transactional
    fun editarJornal(id: Int, jornalDto: JornalDto): JornalResponse {
        val jornal = jornalRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Jornal no encontrado con ID: $id") }

        val empleado = empleadoRepository.findById(jornalDto.empleadoId)
            .orElseThrow { EntityNotFoundException("Empleado no encontrado con ID: ${jornalDto.empleadoId}") }

        val tarea = tareaRepository.findById(jornalDto.tareaId)
            .orElseThrow { EntityNotFoundException("Tarea no encontrada con ID: ${jornalDto.tareaId}") }

        val variedad = variedadRepository.findById(jornalDto.variedadId)
            .orElseThrow { EntityNotFoundException("Variedad no encontrada con ID: ${jornalDto.variedadId}") }

        jornal.empleado = empleado
        jornal.tarea = tarea
        jornal.jornales = jornalDto.jornales
        jornal.fecha = jornalDto.fecha
        jornal.variedad = variedad

        val updatedJornal = jornalRepository.save(jornal)

        return JornalResponse(
            id = updatedJornal.id,
            fecha = updatedJornal.fecha,
            empleadoNombre = updatedJornal.empleado?.nombre ?: "",
            tareaNombre = updatedJornal.tarea?.nombre ?: "",
            jornales = updatedJornal.jornales,
            variedadNombre = updatedJornal.variedad?.nombre ?: ""
        )
    }

    @Transactional
    fun eliminarJornal(id: Int) {
        if (!jornalRepository.existsById(id)) {
            throw EntityNotFoundException("Jornal no encontrado con ID: $id")
        }

        jornalRepository.deleteById(id)
    }

    fun listarJornales(): List<JornalResponse> {
        return jornalRepository.findAll().map { jornal ->
            JornalResponse(
                id = jornal.id,
                fecha = jornal.fecha,
                empleadoNombre = jornal.empleado?.nombre ?: "",
                tareaNombre = jornal.tarea?.nombre ?: "",
                jornales = jornal.jornales,
                variedadNombre = jornal.variedad?.nombre ?: ""
            )
        }
    }
}