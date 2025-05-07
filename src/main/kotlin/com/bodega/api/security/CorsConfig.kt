// src/main/kotlin/com/bodega/api/config/CorsConfig.kt
package com.bodega.api.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig {

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()

        // Permitir solicitudes desde tu frontend
        config.allowedOrigins = listOf("http://localhost:5173")

        // Permitir todos los métodos HTTP (GET, POST, PUT, DELETE, etc.)
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")

        // Permitir todos los encabezados comunes y Authorization
        config.allowedHeaders = listOf(
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        )

        // Permitir credenciales (cookies, auth headers)
        config.allowCredentials = true

        // Cuánto tiempo se cachean los resultados de preflight
        config.maxAge = 3600L

        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    // Config adicional si usas WebMvcConfigurer
    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5173")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600)
            }
        }
    }
}