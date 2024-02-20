package com.chrzanekk.kotlindemoproject.integrationtests

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.payload.*
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerIntegrationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    companion object {

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


    @Test
    fun shouldCreateNewCustomer() {
        val newCustomerRequest = NewCustomerRequest("John", "Doe", "858585")

        val result = this.webTestClient.method(HttpMethod.POST)
            .uri("/api/customer/add")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(newCustomerRequest))
            .exchange()
            .expectStatus().isOk
            .expectBody(NewCustomerResponse::class.java)
            .returnResult().responseBody

        assertThat(result?.firstName).isEqualTo(newCustomerRequest.firstName)
        assertThat(result?.lastName).isEqualTo(newCustomerRequest.lastName)
        assertThat(result?.personalNumber).isEqualTo(newCustomerRequest.personalNumber)
        assertThat(result?.id).isNotNull()
        assertThat(result?.id).isNotZero()
    }

    @Test
    fun shouldReturnListOfTwoCustomersForGivenListOfIds() {
        val getCustomersRequest = GetCustomersRequest(listOf(1L,2L))

        val result = this.webTestClient.method(HttpMethod.GET)
            .uri("/api/customer/filtered")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(getCustomersRequest))
            .exchange()
            .expectStatus().isOk
            .expectBody(GetCustomersResponse::class.java)
            .returnResult().responseBody


        assertThat(result?.customers).isNotNull()
        assertThat(result?.customers).asList()
        assertThat(result?.customers?.size).isEqualTo(2)
    }
    @Test
    fun shouldReturnEmptyListForGivenListOfBadIds() {
        val getCustomersRequest = GetCustomersRequest(listOf(666L,777L))

        val result = this.webTestClient.method(HttpMethod.GET)
            .uri("/api/customer/filtered")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(getCustomersRequest))
            .exchange()
            .expectStatus().isOk
            .expectBody(GetCustomersResponse::class.java)
            .returnResult().responseBody


        assertThat(result?.customers).isNotNull()
        assertThat(result?.customers).asList()
        assertThat(result?.customers?.size).isEqualTo(0)
    }

}