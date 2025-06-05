package com.bodega.api.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "indicadorReporte")
data class IndicadorReporte(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idIndicadorReporte")
    val id: Int? = 0,

    @Column(name = "estructura")
    var estructura: String = "",

    @Column(name = "totalProductivo")
    var totalProductivo: String = "",

    @Column(name = "jornalesNoPorductivos")
    var jornalesNoProductivos: String = "",

    @Column(name = "jornalesPagados")
    var jornalesPagados: String = "",

    @Column(name = "quintalPorJornal")
    var quintalPorJornal: String = "",

    @Column(name = "rendimiento")
    var rendimiento: String = "",
    @Column
    var tipoIndicador: TipoIndicador? = null,
    @Column
    var anio:Int? = null,
    @ManyToOne
    @JoinColumn(name = "idCuartel")
    var cuartel: Cuartel? = null,

    @Column(name = "fechaCarga")
    var fechaCarga: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "idVariedad")
    var variedad: VariedadUva? = null,
)

enum class TipoIndicador {
    VARIEDAD,CUARTEL,PARRAL,ESPALDERO,GENERAL
}