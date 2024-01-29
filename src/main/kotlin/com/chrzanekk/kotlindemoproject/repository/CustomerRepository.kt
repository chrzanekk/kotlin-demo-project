package com.chrzanekk.kotlindemoproject.repository

import com.chrzanekk.kotlindemoproject.domain.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<Customer, Long> {

    fun findByPersonalNumber(personalNumber: String): Customer

}