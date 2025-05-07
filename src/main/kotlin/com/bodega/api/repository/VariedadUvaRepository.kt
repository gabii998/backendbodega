package com.bodega.api.repository

import com.bodega.api.model.VariedadUva
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VariedadUvaRepository : JpaRepository<VariedadUva, Int>