// src/main/kotlin/com/bodega/api/repository/UserRepository.kt
package com.bodega.api.repository

import com.bodega.api.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findByEmail(email: String): Optional<User>
}