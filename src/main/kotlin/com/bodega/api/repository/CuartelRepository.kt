package com.bodega.api.repository

import com.bodega.api.model.Cuartel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CuartelRepository : JpaRepository<Cuartel, Int>