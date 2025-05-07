package com.bodega.api.model

import jakarta.persistence.*

@Entity
@Table(name = "empleado")
data class Empleado(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEmpleado")
    val id: Int = 0,

    @Column(name = "nombre")
    var nombre: String = "",

    @Column(name = "dni", unique = true)
    var dni: String = "",

    @ManyToOne
    @JoinColumn(name = "idFinca")
    var finca: Finca? = null
)