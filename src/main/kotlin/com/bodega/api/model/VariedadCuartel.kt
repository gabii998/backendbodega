package com.bodega.api.model

import jakarta.persistence.*

@Entity
@Table(name = "variedadCuartel")
data class VariedadCuartel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVariedadCuartel")
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "idVariedad")
    var variedad: VariedadUva? = null,

    @Column(name = "superficie")
    var superficie: Double = 0.0,

    @Column(name = "hileras")
    var hileras: Int = 0,

    @ManyToOne
    @JoinColumn(name = "idCuartel")
    var cuartel: Cuartel? = null
)