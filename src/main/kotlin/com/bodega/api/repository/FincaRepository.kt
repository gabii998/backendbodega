package com.bodega.api.repository

import com.bodega.api.model.Finca
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FincaRepository : JpaRepository<Finca, Int>
