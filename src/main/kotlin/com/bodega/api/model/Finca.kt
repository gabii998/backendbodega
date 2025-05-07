package com.bodega.api.model

import jakarta.persistence.*

@Entity
@Table(name = "finca")
data class Finca(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFinca")
    val id: Int = 0,

    @Column(name = "nombre")
    var nombre: String = "",

    @OneToMany(mappedBy = "finca", fetch = FetchType.LAZY)
    var cuarteles: MutableList<Cuartel> = mutableListOf()
)