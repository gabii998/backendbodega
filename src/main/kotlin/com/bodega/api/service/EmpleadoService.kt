package com.bodega.api.service

import com.bodega.api.dto.EmpleadoDto
import com.bodega.api.dto.EmpleadoResponse
import com.bodega.api.model.Empleado
import com.bodega.api.repository.EmpleadoRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmpleadoService(private val empleadoRepository: EmpleadoRepository) {

    @Transactional
    fun crearEmpleado(empleadoDto: EmpleadoDto): EmpleadoResponse {
        val empleado = Empleado(
            nombre = empleadoDto.nombre,
            dni = empleadoDto.dni
        )

        val savedEmpleado = empleadoRepository.save(empleado)

        return EmpleadoResponse(
            id = savedEmpleado.id,
            nombre = savedEmpleado.nombre,
            dni = savedEmpleado.dni
        )
    }

    @Transactional
    fun editarEmpleado(id: Int, empleadoDto: EmpleadoDto): EmpleadoResponse {
        val empleado = empleadoRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Empleado no encontrado con ID: $id") }

        empleado.nombre = empleadoDto.nombre
        empleado.dni = empleadoDto.dni

        val updatedEmpleado = empleadoRepository.save(empleado)

        return EmpleadoResponse(
            id = updatedEmpleado.id,
            nombre = updatedEmpleado.nombre,
            dni = updatedEmpleado.dni
        )
    }

    @Transactional
    fun eliminarEmpleado(id: Int) {
        if (!empleadoRepository.existsById(id)) {
            throw EntityNotFoundException("Empleado no encontrado con ID: $id")
        }

        empleadoRepository.deleteById(id)
    }

    fun listarEmpleados(): List<EmpleadoResponse> {
        return empleadoRepository.findAll().map { empleado ->
            EmpleadoResponse(
                id = empleado.id,
                nombre = empleado.nombre,
                dni = empleado.dni
            )
        }
    }

    fun obtenerEmpleado(id: Int): EmpleadoResponse {
        val empleado = empleadoRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Empleado no encontrado con ID: $id") }

        return EmpleadoResponse(
            id = empleado.id,
            nombre = empleado.nombre,
            dni = empleado.dni
        )
    }
}