package com.bodega.api.service

import com.bodega.api.dto.AuthRequest
import com.bodega.api.dto.AuthResponse
import com.bodega.api.repository.UserRepository
import com.bodega.api.security.JwtTokenUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtTokenUtil: JwtTokenUtil,
    private val userRepository: UserRepository
) {

    fun authenticate(authRequest: AuthRequest): AuthResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password
            )
        )

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(authRequest.email)
        val token: String = jwtTokenUtil.generateToken(userDetails)
        val user = userRepository.findByEmail(authRequest.email).orElseThrow()

        return AuthResponse(
            token = token,
            email = user.email,
            nombre = user.nombre
        )
    }
}