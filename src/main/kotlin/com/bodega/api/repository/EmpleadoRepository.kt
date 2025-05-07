package com.bodega.api.repository

import com.bodega.api.model.Empleado
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface EmpleadoRepository : JpaRepository<Empleado, Int> {
    fun findByDni(dni: String): Optional<Empleado>
}