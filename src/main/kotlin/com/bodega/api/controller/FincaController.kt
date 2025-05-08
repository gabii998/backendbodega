package com.bodega.api.controller

import com.bodega.api.dto.FincaDto
import com.bodega.api.service.FincaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/fincas")
class FincaController(private val fincaService: FincaService) {

    @GetMapping
    fun listarFincas(): ResponseEntity<List<FincaDto>> {
        val fincas = fincaService.listarFincas()
        return ResponseEntity.ok(fincas)
    }

    @GetMapping("/{id}")
    fun obtenerFinca(@PathVariable id: Int): ResponseEntity<FincaDto> {
        val finca = fincaService.obtenerFinca(id)
        return ResponseEntity.ok(finca)
    }

    @PostMapping
    fun crearFinca(@RequestBody fincaDto: FincaDto): ResponseEntity<FincaDto> {
        val finca = fincaService.crearFinca(fincaDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(finca)
    }

    @PutMapping("/{id}")
    fun editarFinca(
        @PathVariable id: Int,
        @RequestBody fincaDto: FincaDto
    ): ResponseEntity<FincaDto> {
        val finca = fincaService.editarFinca(id, fincaDto)
        return ResponseEntity.ok(finca)
    }

    @DeleteMapping("/{id}")
    fun eliminarFinca(@PathVariable id: Int): ResponseEntity<Void> {
        fincaService.eliminarFinca(id)
        return ResponseEntity.noContent().build()
    }
}