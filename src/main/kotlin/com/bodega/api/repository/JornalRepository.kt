package com.bodega.api.repository

import com.bodega.api.dto.JornalResponse
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
    @Query("SELECT j FROM Jornal j WHERE j.cuartel.id = :idCuartel AND j.variedad.id = :idVariedad AND j.fecha BETWEEN :inicio AND :fin")
    fun findByCuartelAndVariedadAndFechaBetween(
        @Param("idCuartel") cuartelId: Int,
        @Param("idVariedad") idVariedad: Int,
        @Param("inicio") inicio: LocalDateTime,
        @Param("fin") fin: LocalDateTime
    ): List<Jornal>

    @Query("SELECT j FROM Jornal j WHERE j.cuartel.id = :idCuartel AND j.fecha BETWEEN :inicio AND :fin")
    fun findByCuartelAndFechaBetween(
        @Param("idCuartel") idCuartel: Int,
        @Param("inicio") inicio: LocalDateTime,
        @Param("fin") fin: LocalDateTime
    ): List<Jornal>

    @Query("SELECT j FROM Jornal j WHERE j.cuartel.id = :idCuartel")
    fun findAllByCuartelId(@Param("idCuartel") cuartelId:Long): List<Jornal>


}