package com.bodega.api.config

import com.bodega.api.model.User
import com.bodega.api.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        // Crear usuario administrador por defecto si no existe
        if (!userRepository.findByEmail("admin@bodega.com").isPresent) {
            val admin = User(
                email = "admin@bodega.com",
                nombre = "Administrador",
                password = passwordEncoder.encode("admin123")
            )
            userRepository.save(admin)
        }
    }
}