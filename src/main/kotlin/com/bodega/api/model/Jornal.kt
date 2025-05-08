package com.bodega.api.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "jornal")
data class Jornal(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idJornal")
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "idEmpleado")
    var empleado: Empleado? = null,

    @ManyToOne
    @JoinColumn(name = "idTarea")
    var tarea: Tarea? = null,

    @Column(name = "jornales")
    var jornales: Double = 0.0,

    @Column(name = "fecha")
    var fecha: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "cuartelId")
    var cuartel: Cuartel? = null,

    @ManyToOne
    @JoinColumn(name = "cargadoPor")
    var cargadoPor: Empleado? = null,

    @ManyToOne
    @JoinColumn(name = "idVariedad")
    var variedad: VariedadUva? = null
)