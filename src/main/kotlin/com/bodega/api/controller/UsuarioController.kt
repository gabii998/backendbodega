// src/main/kotlin/com/bodega/api/controller/UsuarioController.kt
package com.bodega.api.controller

import com.bodega.api.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/usuarios")
class UsuarioController(
    private val userRepository: UserRepository
) {
    @GetMapping("/perfil")
    fun obtenerPerfil(): ResponseEntity<Map<String, Any>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name

        val user = userRepository.findByEmail(email).orElseThrow {
            RuntimeException("Usuario no encontrado")
        }

        val perfil = mapOf(
            "id" to user.id,
            "email" to user.email,
            "nombre" to user.nombre
        )

        return ResponseEntity.ok(perfil)
    }
}