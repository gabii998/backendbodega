package com.bodega.api.controller

import com.bodega.api.dto.JornalDto
import com.bodega.api.dto.JornalResponse
import com.bodega.api.security.JwtTokenUtil
import com.bodega.api.service.JornalService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/jornales")
class JornalController(
    private val jornalService: JornalService,
    private val jwtTokenUtil: JwtTokenUtil
) {

    @PostMapping
    fun crearJornal(
        @RequestBody jornalDto: JornalDto,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<JornalResponse> {
        val token = authHeader.substring(7)
        val email = jwtTokenUtil.getUsernameFromToken(token)

        // Aquí deberías obtener el ID del usuario a partir del email
        // Para simplificar, usamos un ID fijo (esto debería implementarse adecuadamente)
        val usuarioId = 1

        val jornal = jornalService.crearJornal(jornalDto, usuarioId)
        return ResponseEntity.status(HttpStatus.CREATED).body(jornal)
    }

    @PutMapping("/{id}")
    fun editarJornal(
        @PathVariable id: Int,
        @RequestBody jornalDto: JornalDto
    ): ResponseEntity<JornalResponse> {
        val jornal = jornalService.editarJornal(id, jornalDto)
        return ResponseEntity.ok(jornal)
    }

    @DeleteMapping("/{id}")
    fun eliminarJornal(@PathVariable id: Int): ResponseEntity<Void> {
        jornalService.eliminarJornal(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun listarJornales(): ResponseEntity<List<JornalResponse>> {
        val jornales = jornalService.listarJornales()
        return ResponseEntity.ok(jornales)
    }
}