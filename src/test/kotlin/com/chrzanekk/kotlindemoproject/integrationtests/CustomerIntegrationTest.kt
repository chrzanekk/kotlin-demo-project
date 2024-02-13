package com.chrzanekk.kotlindemoproject.integrationtests

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.payload.SearchCustomerRequest
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import org.junit.jupiter.api.AfterAll
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerIntegrationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var customerRepository: CustomerRepository
    init {
       val customerFixture = CustomerFixture(customerRepository)
        customerFixture.addMultipleCustomers()
    }

    companion object{

        @Container
        val container = PostgreSQLContainer<Nothing>("postgres:12").apply {
            withDatabaseName("testDB")
            withUsername("user")
            withPassword("password")
        }


        @DynamicPropertySource
        @JvmStatic
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.password", container::getPassword)
            registry.add("spring.datasource.username", container::getUsername)
        }

        @JvmStatic
        @BeforeAll
        internal fun init() {
            container.start()
        }

        @JvmStatic
        @AfterAll
        internal fun close() {
            container.stop()
        }

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