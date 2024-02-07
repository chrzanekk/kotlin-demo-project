package com.chrzanekk.kotlindemoproject.integrationtests

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository

class CustomerFixture(private val customerRepository: CustomerRepository) {

    fun addMultipleCustomers() {
        customerRepository.saveAll(
            listOf(
                Customer(0L, "John", "Doe", "808080"),
                Customer(0L, "Jimmy", "Black", "828282"),
                Customer(0L, "Samantha", "Smith", "838383")
            )
        )
    }
}