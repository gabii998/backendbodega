package com.bodega.api.repository

import com.bodega.api.model.Cuartel
import com.bodega.api.model.IndicadorReporte
import com.bodega.api.model.VariedadUva
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.List

@Repository
interface IndicadorReporteRepository : JpaRepository<IndicadorReporte, Int> {
    fun findByCuartelAndFechaCargaBetween(cuartel: Cuartel, inicio: LocalDateTime, fin: LocalDateTime): List<IndicadorReporte>

    fun findByCuartelAndVariedadAndFechaCargaBetween(cuartel: Cuartel, variedad: VariedadUva, inicio: LocalDateTime, fin: LocalDateTime): List<IndicadorReporte>
}