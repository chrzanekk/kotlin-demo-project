package com.chrzanekk.kotlindemoproject.integrationtests

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.payload.SearchCustomerRequest
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerIntegrationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    companion object {

        @Container
        val container = PostgreSQLContainer<Nothing>("postgres:12").apply {
            withDatabaseName("testDB")
            withUsername("user")
            withPassword("password")
        }


        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl);
            registry.add("spring.datasource.password", container::getPassword);
            registry.add("spring.datasource.username", container::getUsername);
        }

    }

    @BeforeEach
    fun init() {
        customerRepository.deleteAll()
        CustomerFixture(customerRepository).addMultipleCustomers()
    }


    @Test
    fun shouldFindCustomerByPersonalNumber() {
        val searchCustomerRequest = SearchCustomerRequest("808080")
        val expectedCustomer = Customer(1L, "John", "Doe", "808080")

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