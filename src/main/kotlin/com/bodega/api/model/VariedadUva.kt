package com.bodega.api.model

import jakarta.persistence.*

@Entity
@Table(name = "variedadUva")
data class VariedadUva(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVariedad")
    val id: Int = 0,

    @Column(name = "nombre")
    var nombre: String = ""
)