package com.bodega.api.repository

import com.bodega.api.model.Cuartel
import com.bodega.api.model.SistemaCultivo
import com.bodega.api.model.VariedadCuartel
import com.bodega.api.model.VariedadUva
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VariedadCuartelRepository : JpaRepository<VariedadCuartel, Int> {
    @Query("SELECT v FROM VariedadCuartel v WHERE v.cuartel.sistema = :sistema")
    fun findBySistema(sistema:SistemaCultivo): List<VariedadCuartel>
    fun findByCuartel(cuartel: Cuartel): List<VariedadCuartel>
    fun findByCuartelAndVariedad(cuartel: Cuartel, variedad: VariedadUva): Optional<VariedadCuartel>
}
