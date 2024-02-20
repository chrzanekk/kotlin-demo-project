package com.chrzanekk.kotlindemoproject.integrationtests

import com.chrzanekk.kotlindemoproject.payload.GetCreditsResponse
import com.chrzanekk.kotlindemoproject.payload.NewCreditRequest
import com.chrzanekk.kotlindemoproject.payload.NewCreditResponse
import com.chrzanekk.kotlindemoproject.repository.CreditRepository
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import com.chrzanekk.kotlindemoproject.service.dto.CreditDTO
import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO
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
import java.math.BigDecimal
import java.time.Duration

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreditIntegrationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var creditRepository: CreditRepository

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
    fun requestToCreateNewCreditShouldFindCustomerByPersonalIdAndReturnCreditWithThisCustomer() {
        val customerPersonalNumber = "808080"
        val creditValue = BigDecimal(25000)
        val creditName = "NewCreditForCustomer"
        val customer = CustomerDTO(0L, "John", "Doe", customerPersonalNumber)
        val creditRequest = CreditDTO(0L, creditName, 0L, creditValue)

        val newCreditRequest = NewCreditRequest(customer, creditRequest)

        val customersBefore = customerRepository.findAll();
        val creditBefore = creditRepository.findAll()


        val result = this.webTestClient.method(HttpMethod.POST)
            .uri("/api/credit/create")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(newCreditRequest))
            .exchange()
            .expectStatus().isOk
            .expectBody(NewCreditResponse::class.java)
            .returnResult().responseBody


        val customersAfter = customerRepository.findAll();
        assertThat(customersAfter.size).isEqualTo(customersBefore.size)

        val creditAfter = creditRepository.findAll()
        assertThat(creditAfter.size).isEqualTo(creditBefore.size + 1)

        val lastIndex = creditAfter.lastIndex
        val lastCredit = creditAfter[lastIndex]
        assertThat(result?.creditId).isNotNull()
        assertThat(result?.creditId).isEqualTo(lastCredit.id)
        assertThat(lastCredit.creditName).isEqualTo(creditName)
        assertThat(lastCredit.creditValue).isEqualTo(creditValue)
    }

    @Test
    fun requestToCreateNewCreditShouldNotFindCustomerByPersonalIdAndReturnCreditWithNewCustomer() {
        val customerPersonalNumber = "868686"
        val customer = CustomerDTO(0L, "Will", "Morris", customerPersonalNumber)
        val creditRequest = CreditDTO(0L, "NewCreditForCustomer", 0L, BigDecimal(35000))

        val newCreditRequest = NewCreditRequest(customer, creditRequest)

        val customersBefore = customerRepository.findAll();
        val creditBefore = creditRepository.findAll()


        val result = this.webTestClient.method(HttpMethod.POST)
            .uri("/api/credit/create")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(newCreditRequest))
            .exchange()
            .expectStatus().isOk
            .expectBody(NewCreditResponse::class.java)
            .returnResult().responseBody


        val customersAfter = customerRepository.findAll();
        assertThat(customersAfter.size).isEqualTo(customersBefore.size + 1)

        val lastCustomerIndex = customersAfter.lastIndex
        val lastCustomer = customersAfter[lastCustomerIndex]

        val creditAfter = creditRepository.findAll()
        assertThat(creditAfter.size).isEqualTo(creditBefore.size + 1)

        val lastCreditIndex = creditAfter.lastIndex
        val lastCredit = creditAfter[lastCreditIndex]
        assertThat(result?.creditId).isNotNull()
        assertThat(result?.creditId).isEqualTo(lastCredit.id)

        assertThat(lastCredit.customerId).isEqualTo(lastCustomer.id)
    }

    @Test
    fun requestToFindAllCreditsByListOfIdsShouldReturnCredits() {
        val allCredits = creditRepository.findAll();
        val allCreditsSize = allCredits.size

        val result = this.webTestClient.method(HttpMethod.GET)
            .uri("/api/credit/getCredits")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(GetCreditsResponse::class.java)
            .returnResult().responseBody

        assertThat(result?.credits?.size).isEqualTo(allCreditsSize)
    }

}