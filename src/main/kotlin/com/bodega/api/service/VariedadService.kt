package com.bodega.api.service

import com.bodega.api.dto.VariedadDto
import com.bodega.api.model.VariedadUva
import com.bodega.api.repository.VariedadUvaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VariedadService(private val variedadRepository: VariedadUvaRepository) {

    @Transactional
    fun crearVariedad(variedadDto: VariedadDto): VariedadUva {
        val variedad = VariedadUva(
            nombre = variedadDto.nombre
        )

        return variedadRepository.save(variedad)
    }

    @Transactional
    fun eliminarVariedad(idVariedad:Int) {
        if (!variedadRepository.existsById(idVariedad)) {
            throw EntityNotFoundException("Variedad no encontrada con ID: $idVariedad")
        }
        return variedadRepository.deleteById(idVariedad)
    }

    @Transactional
    fun editarVariedad(id: Int, variedadDto: VariedadDto): VariedadUva {
        val variedad = variedadRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Variedad no encontrada con ID: $id") }

        variedad.nombre = variedadDto.nombre

        return variedadRepository.save(variedad)
    }

    fun listarVariedades(): List<VariedadUva> {
        return variedadRepository.findAll()
    }

    fun obtenerVariedad(id: Int): VariedadUva {
        return variedadRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Variedad no encontrada con ID: $id") }
    }
}