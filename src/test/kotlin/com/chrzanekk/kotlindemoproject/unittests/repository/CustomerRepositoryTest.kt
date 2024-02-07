package com.chrzanekk.kotlindemoproject.unittests.repository

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    lateinit var em: EntityManager

    @Autowired
    lateinit var customerRepository: CustomerRepository


    @Test
    fun whenFindByPersonalNumber_thenReturnCustomer() {
        //given
        val personalNumber = "838383"
        val customer = Customer(0, "John", "Doe", personalNumber)
        em.persist(customer)
        em.flush()

        //when
        val customerFromDB = customerRepository.findByPersonalNumber(personalNumber)

        //then
        assertEquals(customer.personalNumber, customerFromDB!!.personalNumber)
    }

    @Test
    fun whenFindByPersonalNumber_thenReturnCorrectCustomer() {
        //given
        val personalNumber = "838383"
        val customer = Customer(0, "John", "Doe", personalNumber)
        em.persist(customer)
        em.flush()
        val secondCustomer = Customer(0, "Jimmy", "Black", "828282")
        em.persist(secondCustomer)
        em.flush()

        //when
        val customerFromDB = customerRepository.findByPersonalNumber(personalNumber)

        //then
        assertEquals(customer.personalNumber, customerFromDB!!.personalNumber)
        assertNotEquals(secondCustomer.personalNumber, customerFromDB.personalNumber)
    }

    @Test
    fun whenFindByPersonalNumber_thenReturnNull() {
        //given
        val personalNumber = "838383"

        //when
        val customerFromDB = customerRepository.findByPersonalNumber(personalNumber)

        //then
        assertEquals(customerFromDB, null)
    }
}