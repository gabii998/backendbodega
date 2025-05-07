package com.bodega.api.controller

import com.bodega.api.dto.TareaDto
import com.bodega.api.model.Tarea
import com.bodega.api.service.TareaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tareas")
class TareaController(private val tareaService: TareaService) {

    @PostMapping
    fun crearTarea(@RequestBody tareaDto: TareaDto): ResponseEntity<Tarea> {
        val tarea = tareaService.crearTarea(tareaDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(tarea)
    }

    @PutMapping("/{id}")
    fun editarTarea(
        @PathVariable id: Int,
        @RequestBody tareaDto: TareaDto
    ): ResponseEntity<Tarea> {
        val tarea = tareaService.editarTarea(id, tareaDto)
        return ResponseEntity.ok(tarea)
    }

    @DeleteMapping("/{id}")
    fun eliminarTarea(@PathVariable id: Int): ResponseEntity<Void> {
        tareaService.eliminarTarea(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun listarTareas(): ResponseEntity<List<Tarea>> {
        val tareas = tareaService.listarTareas()
        return ResponseEntity.ok(tareas)
    }

    @GetMapping("/{id}")
    fun obtenerTarea(@PathVariable id: Int): ResponseEntity<Tarea> {
        val tarea = tareaService.obtenerTarea(id)
        return ResponseEntity.ok(tarea)
    }
}