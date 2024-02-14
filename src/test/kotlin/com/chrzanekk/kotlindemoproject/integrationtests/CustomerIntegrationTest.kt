package com.chrzanekk.kotlindemoproject.integrationtests

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.payload.SearchCustomerRequest
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerIntegrationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    companion object{

        @Container
        val container = postgres("postgres:12") {
            withDatabaseName("db")
            withUsername("user")
            withPassword("password")
            withInitScript("/schema.sql")
        }


    }

    @Test
    fun containerIsUpAndRunning() {
        Assertions.assertTrue(container.isRunning)

    }


    @Test
    fun shouldFindCustomerByPersonalNumber() {
        val searchCustomerRequest = SearchCustomerRequest("808080")
        val expectedCustomer = Customer(1L, "John", "Doe", "808080")
        val customers = customerRepository.findAll()
        this.webTestClient.method(HttpMethod.GET)
            .uri("api/customer/search")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(searchCustomerRequest))
            .exchange()
            .expectStatus().isOk()
            .expectBody(Customer::class.java)
            .isEqualTo(expectedCustomer)

    }

    @Test
    fun shouldThrowExceptionWhenNotFindCustomerByPersonalNumber() {
        val searchCustomerRequest = SearchCustomerRequest("909090")

        this.webTestClient.method(HttpMethod.GET)
            .uri("api/customer/search")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(searchCustomerRequest))
            .exchange()
            .expectStatus().isBadRequest()


    }

}