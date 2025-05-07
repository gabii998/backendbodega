package com.bodega.api.model

import jakarta.persistence.*

enum class TipoTarea {
    Manual, Mercanica
}

@Entity
@Table(name = "tarea")
data class Tarea(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTarea")
    val id: Int = 0,

    @Column(name = "nombre")
    var nombre: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    var tipo: TipoTarea = TipoTarea.Manual
)