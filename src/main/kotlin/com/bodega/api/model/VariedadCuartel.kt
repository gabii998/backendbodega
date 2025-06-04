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

    @Column(name = "hileras")
    var hileras: Int = 0,

    @ManyToOne
    @JoinColumn(name = "idCuartel")
    var cuartel: Cuartel? = null,

    @OneToMany(mappedBy = "variedadCuartel")
    var historicoSuperficie: MutableList<HistoricoSuperficie> = mutableListOf()
) {
    val superficieActual:Double?
        get() = historicoSuperficie.maxByOrNull { it.ano }?.superficie
}