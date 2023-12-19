package com.chrzanekk.kotlindemoproject.repository

import com.chrzanekk.kotlindemoproject.domain.Credit
import org.springframework.data.jpa.repository.JpaRepository

interface CreditRepository : JpaRepository<Credit, Long> {

    fun save(credit: Credit): Credit
}