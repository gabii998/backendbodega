package com.bodega.api.controller

import com.bodega.api.dto.CuartelDto
import com.bodega.api.dto.CuartelResponse
import com.bodega.api.service.CuartelService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cuarteles")
class CuartelController(private val cuartelService: CuartelService) {

    @PostMapping
    fun crearCuartel(@RequestBody cuartelDto: CuartelDto): ResponseEntity<CuartelResponse> {
        val cuartel = cuartelService.crearCuartel(cuartelDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(cuartel)
    }

    @PutMapping("/{id}")
    fun editarCuartel(
        @PathVariable id: Int,
        @RequestBody cuartelDto: CuartelDto
    ): ResponseEntity<CuartelResponse> {
        val cuartel = cuartelService.editarCuartel(id, cuartelDto)
        return ResponseEntity.ok(cuartel)
    }

    @DeleteMapping("/{id}")
    fun eliminarCuartel(@PathVariable id: Int): ResponseEntity<Void> {
        cuartelService.eliminarCuartel(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun listarCuarteles(
        @RequestParam(required = false) fincaId: Int?,
    ): ResponseEntity<List<CuartelResponse>> {
        val cuarteles = cuartelService.listarCuarteles(fincaId)
        return ResponseEntity.ok(cuarteles)
    }

    @GetMapping("/{id}")
    fun obtenerCuartel(@PathVariable id: Int): ResponseEntity<CuartelResponse> {
        val cuartel = cuartelService.obtenerCuartel(id)
        return ResponseEntity.ok(cuartel)
    }
}