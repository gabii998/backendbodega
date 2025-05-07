package com.bodega.api.model

import jakarta.persistence.*

enum class SistemaCultivo {
    Parral, Espaldero
}

@Entity
@Table(name = "cuartel")
data class Cuartel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCuartel")
    val id: Int = 0,

    @Column(name = "nombre")
    var nombre: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "sistema")
    var sistema: SistemaCultivo = SistemaCultivo.Parral,

    @ManyToOne
    @JoinColumn(name = "idEncargado")
    var encargado: Empleado? = null,

    @ManyToOne
    @JoinColumn(name = "idFinca")
    var finca: Finca? = null,

    @OneToMany(mappedBy = "cuartel", cascade = [CascadeType.ALL], orphanRemoval = true)
    var variedades: MutableList<VariedadCuartel> = mutableListOf()
)