package com.bodega.api.service

import com.bodega.api.dto.FincaDto
import com.bodega.api.model.Finca
import com.bodega.api.repository.FincaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FincaService(private val fincaRepository: FincaRepository) {

    fun listarFincas(): List<FincaDto> {
        return fincaRepository.findAll().map { finca ->
            FincaDto(
                id = finca.id,
                nombre = finca.nombre
            )
        }
    }

    fun obtenerFinca(id: Int): FincaDto {
        val finca = fincaRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Finca no encontrada con ID: $id") }

        return FincaDto(
            id = finca.id,
            nombre = finca.nombre
        )
    }

    @Transactional
    fun crearFinca(fincaDto: FincaDto): FincaDto {
        val finca = Finca(
            nombre = fincaDto.nombre
        )

        val savedFinca = fincaRepository.save(finca)

        return FincaDto(
            id = savedFinca.id,
            nombre = savedFinca.nombre
        )
    }

    @Transactional
    fun editarFinca(id: Int, fincaDto: FincaDto): FincaDto {
        val finca = fincaRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Finca no encontrada con ID: $id") }

        finca.nombre = fincaDto.nombre

        val updatedFinca = fincaRepository.save(finca)

        return FincaDto(
            id = updatedFinca.id,
            nombre = updatedFinca.nombre
        )
    }

    @Transactional
    fun eliminarFinca(id: Int) {
        if (!fincaRepository.existsById(id)) {
            throw EntityNotFoundException("Finca no encontrada con ID: $id")
        }

        fincaRepository.deleteById(id)
    }
}