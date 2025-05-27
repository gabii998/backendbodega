package com.bodega.api

import java.time.LocalDateTime

object Util {
    fun getInicioAnio(ano:Int):LocalDateTime {
        return LocalDateTime.of(ano, 1, 1, 0, 0)
    }

    fun getFinAno(ano:Int):LocalDateTime {
        return LocalDateTime.of(ano, 12, 31, 23, 59, 59)
    }
}