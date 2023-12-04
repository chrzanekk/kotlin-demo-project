package com.chrzanekk.kotlindemoproject.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Customer(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(name = "first_name")
        val firstName: String,

        @Column(name = "last_name")
        val lastName: String,

        @Column(name = "personal_number")
        val personalNumber: String
)
