package com.bodega.api.service

import com.bodega.api.dto.TareaDto
import com.bodega.api.model.Tarea
import com.bodega.api.model.TipoTarea
import com.bodega.api.repository.TareaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TareaService(private val tareaRepository: TareaRepository) {

    @Transactional
    fun crearTarea(tareaDto: TareaDto): Tarea {
        val tarea = Tarea(
            nombre = tareaDto.nombre,
            tipo = TipoTarea.valueOf(tareaDto.tipo)
        )

        return tareaRepository.save(tarea)
    }

    @Transactional
    fun editarTarea(id: Int, tareaDto: TareaDto): Tarea {
        val tarea = tareaRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Tarea no encontrada con ID: $id") }

        tarea.nombre = tareaDto.nombre
        tarea.tipo = TipoTarea.valueOf(tareaDto.tipo)

        return tareaRepository.save(tarea)
    }

    @Transactional
    fun eliminarTarea(id: Int) {
        if (!tareaRepository.existsById(id)) {
            throw EntityNotFoundException("Tarea no encontrada con ID: $id")
        }

        tareaRepository.deleteById(id)
    }

    fun listarTareas(): List<Tarea> {
        return tareaRepository.findAll()
    }

    fun obtenerTarea(id: Int): Tarea {
        return tareaRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Tarea no encontrada con ID: $id") }
    }
}