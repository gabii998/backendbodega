package com.bodega.api.service

import com.bodega.api.dto.CuartelDto
import com.bodega.api.dto.CuartelResponse
import com.bodega.api.dto.VariedadInfoDto
import com.bodega.api.model.Cuartel
import com.bodega.api.model.SistemaCultivo
import com.bodega.api.model.VariedadCuartel
import com.bodega.api.repository.CuartelRepository
import com.bodega.api.repository.EmpleadoRepository
import com.bodega.api.repository.FincaRepository
import com.bodega.api.repository.VariedadCuartelRepository
import com.bodega.api.repository.VariedadUvaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CuartelService(
    private val cuartelRepository: CuartelRepository,
    private val fincaRepository: FincaRepository,
    private val empleadoRepository: EmpleadoRepository,
    private val variedadRepository: VariedadUvaRepository,
    private val variedadCuartelRepository: VariedadCuartelRepository
) {

    @Transactional
    fun crearCuartel(cuartelDto: CuartelDto): CuartelResponse {
        val finca = fincaRepository.findById(cuartelDto.fincaId)
            .orElseThrow { EntityNotFoundException("Finca no encontrada con ID: ${cuartelDto.fincaId}") }

        val encargado = empleadoRepository.findById(cuartelDto.encargadoId)
            .orElseThrow { EntityNotFoundException("Encargado no encontrado con ID: ${cuartelDto.encargadoId}") }

        val cuartel = Cuartel(
            nombre = cuartelDto.nombre,
            sistema = SistemaCultivo.valueOf(cuartelDto.sistema),
            finca = finca,
            encargado = encargado
        )

        val savedCuartel = cuartelRepository.save(cuartel)

        // Asociar variedades al cuartel
        val variedades = cuartelDto.variedades.map { variedadDto ->
            val variedad = variedadRepository.findById(variedadDto.id)
                .orElseThrow { EntityNotFoundException("Variedad no encontrada con ID: ${variedadDto.id}") }

            val variedadCuartel = VariedadCuartel(
                variedad = variedad,
                superficie = variedadDto.superficie,
                cuartel = savedCuartel
            )

            variedadCuartelRepository.save(variedadCuartel)
        }

        return convertirACuartelResponse(savedCuartel)
    }

    @Transactional
    fun editarCuartel(id: Int, cuartelDto: CuartelDto): CuartelResponse {
        val cuartel = cuartelRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $id") }

        val finca = fincaRepository.findById(cuartelDto.fincaId)
            .orElseThrow { EntityNotFoundException("Finca no encontrada con ID: ${cuartelDto.fincaId}") }

        val encargado = empleadoRepository.findById(cuartelDto.encargadoId)
            .orElseThrow { EntityNotFoundException("Encargado no encontrado con ID: ${cuartelDto.encargadoId}") }

        cuartel.nombre = cuartelDto.nombre
        cuartel.sistema = SistemaCultivo.valueOf(cuartelDto.sistema)
        cuartel.finca = finca
        cuartel.encargado = encargado

        // Eliminar variedades existentes
        val variedadesExistentes = variedadCuartelRepository.findByCuartel(cuartel)
        variedadCuartelRepository.deleteAll(variedadesExistentes)

        // Agregar nuevas variedades
        cuartelDto.variedades.forEach { variedadDto ->
            val variedad = variedadRepository.findById(variedadDto.id)
                .orElseThrow { EntityNotFoundException("Variedad no encontrada con ID: ${variedadDto.id}") }

            val variedadCuartel = VariedadCuartel(
                variedad = variedad,
                superficie = variedadDto.superficie,
                cuartel = cuartel,
                hileras = variedadDto.hileras
            )

            variedadCuartelRepository.save(variedadCuartel)
        }

        val updatedCuartel = cuartelRepository.save(cuartel)

        return convertirACuartelResponse(updatedCuartel)
    }

    @Transactional
    fun eliminarCuartel(id: Int) {
        if (!cuartelRepository.existsById(id)) {
            throw EntityNotFoundException("Cuartel no encontrado con ID: $id")
        }

        cuartelRepository.deleteById(id)
    }

    fun listarCuarteles(fincaId:Int?): List<CuartelResponse> {
        if (fincaId != null) {
            return cuartelRepository.findAllByFincaId(fincaId).map { cuartel ->
                convertirACuartelResponse(cuartel)
            }
        }

        return cuartelRepository.findAll().map { cuartel ->
            convertirACuartelResponse(cuartel)
        }
    }

    fun obtenerCuartel(id: Int): CuartelResponse {
        val cuartel = cuartelRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Cuartel no encontrado con ID: $id") }

        return convertirACuartelResponse(cuartel)
    }

    private fun convertirACuartelResponse(cuartel: Cuartel): CuartelResponse {
        val variedades = variedadCuartelRepository.findByCuartel(cuartel)
        val superficieTotal = variedades.sumOf { it.superficie }

        val variedadesInfo = variedades.map { vc ->
            VariedadInfoDto(
                id = vc.variedad?.id ?: 0,
                nombre = vc.variedad?.nombre ?: "",
                superficie = vc.superficie,
                hileras = vc.hileras,
            )
        }

        return CuartelResponse(
            id = cuartel.id,
            nombre = cuartel.nombre,
            sistema = cuartel.sistema.name,
            superficieTotal = superficieTotal,
            encargadoNombre = cuartel.encargado?.nombre ?: "",
            variedades = variedadesInfo,
            hileras = variedades.sumOf { it.hileras },
            encargadoId = cuartel.encargado?.id ?: -1,
        )
    }
}