package com.chrzanekk.kotlindemoproject.unittests.controller

import com.chrzanekk.kotlindemoproject.controller.CreditController
import com.chrzanekk.kotlindemoproject.payload.*
import com.chrzanekk.kotlindemoproject.service.CreditService
import com.chrzanekk.kotlindemoproject.service.CustomerService
import com.chrzanekk.kotlindemoproject.service.dto.CreditDTO
import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

@WebMvcTest(CreditController::class)
@ExtendWith(SpringExtension::class)
internal class CreditControllerTest {


    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun creditService() = mockk<CreditService>()

        @Bean
        fun customerService() = mockk<CustomerService>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var creditService: CreditService

    @Autowired
    private lateinit var customerService: CustomerService

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun requestToCreateNewCreditShouldFindCustomerAndReturnCreditWithThisCustomer() {
        val customerDTO = CustomerDTO(1L, "John", "Doe", "808080")
        val searchCustomer = SearchCustomerRequest(customerDTO.personalNumber)
        val foundCustomer = SearchCustomerResponse(
            customerDTO.id, customerDTO.firstName, customerDTO.lastName,
            customerDTO.personalNumber
        )
        val creditDTO = CreditDTO(0L, "NewCreditName", 0L, BigDecimal(10000))
        val newCreditRequest = NewCreditRequest(customerDTO, creditDTO)
        val expectedCreditResponse = NewCreditResponse(1L)

        every { creditService.createCredit(newCreditRequest) } returns expectedCreditResponse
        every { customerService.findByPersonalNumber(searchCustomer) } returns foundCustomer

        mockMvc.perform(
            post("/api/credit/create")
                .content(objectMapper.writeValueAsString(newCreditRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.creditId").value(expectedCreditResponse.creditId))
    }

    @Test
    fun requestToFindAllCredits() {
        val firstCustomerDTO = CustomerDTO(1L, "John", "Doe", "808080")
        val secondCustomerDTO = CustomerDTO(2L, "Jimmy", "Black", "818181")

        val firstCreditDTO = CreditDTO(1L, "NewCreditName", 1L, BigDecimal(10000))
        val secondCreditDTO = CreditDTO(2L, "SecondNewCreditName", 1L, BigDecimal(20000))
        val thirdCreditDTO = CreditDTO(3L, "ThirdNewCreditName", 2L, BigDecimal(20000))

        val firstGetCreditResponse = GetCreditResponse(firstCustomerDTO,firstCreditDTO)
        val secondGetCreditResponse = GetCreditResponse(firstCustomerDTO,secondCreditDTO)
        val thirdGetCreditResponse = GetCreditResponse(secondCustomerDTO,thirdCreditDTO)

        val expectedGetCreditResponse = GetCreditsResponse(listOf(firstGetCreditResponse,secondGetCreditResponse,thirdGetCreditResponse))

        every { creditService.findAll() } returns expectedGetCreditResponse

        mockMvc.perform(
            get("/api/credit/getCredits"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.credits.[0].customerDTO.id").value(firstCustomerDTO.id))
            .andExpect(jsonPath("$.credits.[0].customerDTO.firstName").value(firstCustomerDTO.firstName))
            .andExpect(jsonPath("$.credits.[0].customerDTO.lastName").value(firstCustomerDTO.lastName))
            .andExpect(jsonPath("$.credits.[0].customerDTO.personalNumber").value(firstCustomerDTO.personalNumber))            .andExpect(jsonPath("$.credits.[0].customerDTO.id").value(firstCustomerDTO.id))
            .andExpect(jsonPath("$.credits.[0].creditDTO.id").value(firstCreditDTO.id))
            .andExpect(jsonPath("$.credits.[0].creditDTO.creditName").value(firstCreditDTO.creditName))
            .andExpect(jsonPath("$.credits.[0].creditDTO.creditValue").value(firstCreditDTO.creditValue))
            .andExpect(jsonPath("$.credits.[1].customerDTO.id").value(firstCustomerDTO.id))
            .andExpect(jsonPath("$.credits.[1].customerDTO.firstName").value(firstCustomerDTO.firstName))
            .andExpect(jsonPath("$.credits.[1].customerDTO.lastName").value(firstCustomerDTO.lastName))
            .andExpect(jsonPath("$.credits.[1].customerDTO.personalNumber").value(firstCustomerDTO.personalNumber))
            .andExpect(jsonPath("$.credits.[1].creditDTO.id").value(secondCreditDTO.id))
            .andExpect(jsonPath("$.credits.[1].creditDTO.creditName").value(secondCreditDTO.creditName))
            .andExpect(jsonPath("$.credits.[1].creditDTO.creditValue").value(secondCreditDTO.creditValue))
            .andExpect(jsonPath("$.credits.[2].customerDTO.id").value(secondCustomerDTO.id))
            .andExpect(jsonPath("$.credits.[2].customerDTO.firstName").value(secondCustomerDTO.firstName))
            .andExpect(jsonPath("$.credits.[2].customerDTO.lastName").value(secondCustomerDTO.lastName))
            .andExpect(jsonPath("$.credits.[2].customerDTO.personalNumber").value(secondCustomerDTO.personalNumber))
            .andExpect(jsonPath("$.credits.[2].creditDTO.id").value(thirdCreditDTO.id))
            .andExpect(jsonPath("$.credits.[2].creditDTO.creditName").value(thirdCreditDTO.creditName))
            .andExpect(jsonPath("$.credits.[2].creditDTO.creditValue").value(thirdCreditDTO.creditValue))
    }
    @Test
    fun requestToFindAllCreditsShouldReturnEmptyListIfNotFound() {


        val expectedGetCreditResponse = GetCreditsResponse(listOf())

        every { creditService.findAll() } returns expectedGetCreditResponse

        mockMvc.perform(
            get("/api/credit/getCredits"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.credits").isArray)
            .andExpect(jsonPath("$.credits").isEmpty)
    }
}