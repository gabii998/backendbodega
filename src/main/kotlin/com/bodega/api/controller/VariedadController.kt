package com.bodega.api.controller

import com.bodega.api.dto.VariedadDto
import com.bodega.api.model.VariedadUva
import com.bodega.api.service.VariedadService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/variedades")
class VariedadController(private val variedadService: VariedadService) {

    @PostMapping
    fun crearVariedad(@RequestBody variedadDto: VariedadDto): ResponseEntity<VariedadUva> {
        val variedad = variedadService.crearVariedad(variedadDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(variedad)
    }

    @PutMapping("/{id}")
    fun editarVariedad(
        @PathVariable id: Int,
        @RequestBody variedadDto: VariedadDto
    ): ResponseEntity<VariedadUva> {
        val variedad = variedadService.editarVariedad(id, variedadDto)
        return ResponseEntity.ok(variedad)
    }

    @GetMapping
    fun listarVariedades(): ResponseEntity<List<VariedadUva>> {
        val variedades = variedadService.listarVariedades()
        return ResponseEntity.ok(variedades)
    }
}