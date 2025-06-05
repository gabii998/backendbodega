package com.bodega.api.repository

import com.bodega.api.model.Cuartel
import com.bodega.api.model.IndicadorReporte
import com.bodega.api.model.TipoIndicador
import com.bodega.api.model.VariedadUva
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface IndicadorReporteRepository : JpaRepository<IndicadorReporte, Int> {
    @Query("SELECT j FROM IndicadorReporte j WHERE j.tipoIndicador = :tipoIndicador AND j.anio=:anio")
    fun findIndicadorGeneral(anio:Int,tipoIndicador: TipoIndicador = TipoIndicador.GENERAL): Optional<IndicadorReporte>
    fun findByCuartelAndFechaCarga(cuartel: Cuartel, fechaCarga: LocalDateTime): List<IndicadorReporte>
    fun findByCuartelAndFechaCargaBetween(cuartel: Cuartel, inicio: LocalDateTime, fin: LocalDateTime): List<IndicadorReporte>
    fun findByCuartelAndVariedadAndFechaCargaBetween(cuartel: Cuartel, variedad: VariedadUva, inicio: LocalDateTime, fin: LocalDateTime): List<IndicadorReporte>
}