package com.chrzanekk.kotlindemoproject.unittests.repository

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.integrationtests.CustomerIntegrationTest
import com.chrzanekk.kotlindemoproject.integrationtests.postgres
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerRepositoryTest {


    @Autowired
    private lateinit var customerRepository: CustomerRepository

    companion object{

        @Container
        val container = postgres("12") {
            withDatabaseName("db")
            withUsername("user")
            withPassword("password")
            withInitScript("schema.sql")
        }

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
        }
    }

    @Test
    fun containerIsUpAndRunning() {
        Assertions.assertTrue(container.isRunning)
    }

    @Test
    fun whenFindByPersonalNumber_thenReturnCustomer() {
        //given
        val personalNumber = "808080"

        //when
        val customerFromDB = customerRepository.findByPersonalNumber(personalNumber)

        //then
        if (customerFromDB != null) {
            assertEquals(personalNumber, customerFromDB.personalNumber)
        }
    }

    @Test
    fun whenFindByPersonalNumber_thenReturnCorrectCustomer() {
        //given
        val personalNumber = "838383"

        //when
        val customerFromDB = customerRepository.findByPersonalNumber(personalNumber)

        //then
        if (customerFromDB != null) {
            assertNotEquals(personalNumber, customerFromDB.personalNumber)
        }
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