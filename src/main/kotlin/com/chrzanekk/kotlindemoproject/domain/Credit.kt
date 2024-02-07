package com.chrzanekk.kotlindemoproject.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "credit")
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
