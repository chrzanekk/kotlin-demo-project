package com.chrzanekk.kotlindemoproject.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity
data class Credit(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        @Column(name = "credit_name")
        val creditName: String,

        @Column(name = "customer_id")
        val customerId: Long,

        @Column(name = "credit_value")
        val creditValue: BigDecimal
)
