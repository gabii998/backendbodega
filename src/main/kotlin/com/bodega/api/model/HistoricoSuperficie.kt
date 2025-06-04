package com.bodega.api.model

import jakarta.persistence.*

@Entity
@Table()
data class HistoricoSuperficie(
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id:Int? = null,
    @Column
    val superficie:Double,
    @Column
    val ano:Int,
    @ManyToOne
    @JoinColumn(name = "idVariedadCuartel")
    var variedadCuartel: VariedadCuartel? = null
)