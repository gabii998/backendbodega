package com.bodega.api.model

import jakarta.persistence.*

@Entity
@Table(name = "usuarios")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUser")
    val id: Int = 0,

    @Column(name = "email", unique = true)
    val email: String = "",

    @Column(name = "nombre")
    var nombre: String = "",

    @Column(name = "password")
    var password: String = ""
)