package com.bodega.api.repository

import com.bodega.api.model.HistoricoSuperficie
import com.bodega.api.model.Jornal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface SuperficieRepository: JpaRepository<HistoricoSuperficie, Int> {
    @Query("DELETE FROM HistoricoSuperficie WHERE variedadCuartel.id = :id and ano = :ano")
    @Modifying
    @Transactional
    fun deleteByYearAndVariedadCuartelId(@Param("id") variedadCuartelId: Int,@Param("ano") ano: Int)

    @Query("SELECT s from HistoricoSuperficie s where s.variedadCuartel.cuartel.id = :id")
    fun findByCuartelId(@Param("id") id: Int): List<HistoricoSuperficie>
}