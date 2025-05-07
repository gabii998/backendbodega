package com.bodega.api.dto

data class AuthResponse(
    val token: String,
    val email: String,
    val nombre: String
)