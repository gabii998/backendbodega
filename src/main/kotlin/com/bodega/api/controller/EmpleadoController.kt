package com.bodega.api.controller

import com.bodega.api.dto.EmpleadoDto
import com.bodega.api.dto.EmpleadoResponse
import com.bodega.api.service.EmpleadoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/empleados")
class EmpleadoController(private val empleadoService: EmpleadoService) {

    @PostMapping
    fun crearEmpleado(@RequestBody empleadoDto: EmpleadoDto): ResponseEntity<EmpleadoResponse> {
        val empleado = empleadoService.crearEmpleado(empleadoDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(empleado)
    }

    @PutMapping("/{id}")
    fun editarEmpleado(
        @PathVariable id: Int,
        @RequestBody empleadoDto: EmpleadoDto
    ): ResponseEntity<EmpleadoResponse> {
        val empleado = empleadoService.editarEmpleado(id, empleadoDto)
        return ResponseEntity.ok(empleado)
    }

    @DeleteMapping("/{id}")
    fun eliminarEmpleado(@PathVariable id: Int): ResponseEntity<Void> {
        empleadoService.eliminarEmpleado(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun listarEmpleados(): ResponseEntity<List<EmpleadoResponse>> {
        val empleados = empleadoService.listarEmpleados()
        return ResponseEntity.ok(empleados)
    }

    @GetMapping("/{id}")
    fun obtenerEmpleado(@PathVariable id: Int): ResponseEntity<EmpleadoResponse> {
        val empleado = empleadoService.obtenerEmpleado(id)
        return ResponseEntity.ok(empleado)
    }
}