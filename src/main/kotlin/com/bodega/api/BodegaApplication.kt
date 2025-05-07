package com.bodega.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BodegaApplication

fun main(args: Array<String>) {
    runApplication<BodegaApplication>(*args)
}