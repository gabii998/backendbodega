package com.bodega.api.repository

import com.bodega.api.model.Tarea
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TareaRepository : JpaRepository<Tarea, Int>