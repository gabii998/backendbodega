package com.bodega.api.repository

import com.bodega.api.model.Cuartel
import com.bodega.api.model.Empleado
import com.bodega.api.model.Jornal
import com.bodega.api.model.VariedadUva
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface JornalRepository : JpaRepository<Jornal, Int> {
    fun findByEmpleado(empleado: Empleado): List<Jornal>

    fun findByFechaBetween(inicio: LocalDateTime, fin: LocalDateTime): List<Jornal>

    // Consulta corregida para encontrar jornales por cuartel y rango de fechas
    @Query("SELECT j FROM Jornal j WHERE j.empleado.finca IN (SELECT c.finca FROM Cuartel c WHERE c = :cuartel) AND j.variedad = :variedad AND j.fecha BETWEEN :inicio AND :fin")
    fun findByCuartelAndVariedadAndFechaBetween(
        @Param("cuartel") cuartel: Cuartel,
        @Param("variedad") variedad: VariedadUva,
        @Param("inicio") inicio: LocalDateTime,
        @Param("fin") fin: LocalDateTime
    ): List<Jornal>

    @Query("SELECT j FROM Jornal j WHERE j.empleado.finca IN (SELECT c.finca FROM Cuartel c WHERE c = :cuartel) AND j.fecha BETWEEN :inicio AND :fin")
    fun findByCuartelAndFechaBetween(
        @Param("cuartel") cuartel: Cuartel,
        @Param("inicio") inicio: LocalDateTime,
        @Param("fin") fin: LocalDateTime
    ): List<Jornal>


}