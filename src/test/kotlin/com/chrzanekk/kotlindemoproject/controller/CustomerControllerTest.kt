package com.chrzanekk.kotlindemoproject.controller

import com.chrzanekk.kotlindemoproject.payload.SearchCustomerRequest
import com.chrzanekk.kotlindemoproject.payload.SearchCustomerResponse
import com.chrzanekk.kotlindemoproject.service.CustomerService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@WebMvcTest(CustomerController::class)
@ExtendWith(SpringExtension::class)
internal class CustomerControllerTest {

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun service() = mockk<CustomerService>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var service: CustomerService

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun requestToFindCustomerShouldReturnEntityWithCorrectResponse() {
        val personalNumber = "808080"
        val expectedResponse = SearchCustomerResponse(1L, "John", "Doe", personalNumber)
        val searchCustomerRequest = SearchCustomerRequest(personalNumber)

        every { service.findByPersonalNumber(searchCustomerRequest) } returns expectedResponse

        val result = mockMvc.perform(
            get("/api/customer/search")
                .content(objectMapper.writeValueAsString(searchCustomerRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(expectedResponse.id))
            .andExpect(jsonPath("$.firstName").value(expectedResponse.firstName))
            .andExpect(jsonPath("$.lastName").value(expectedResponse.lastName))
            .andExpect(jsonPath("$.personalNumber").value(expectedResponse.personalNumber))
    }
//todo figureout how to test throwing exception on controller layer in kotlin spring boot
    @Test
    fun requestToFindCustomerShouldThrowExceptionForNotFound() {
        val personalNumber = "808080"
        val searchCustomerRequest = SearchCustomerRequest(personalNumber)

        every { service.findByPersonalNumber(searchCustomerRequest) } throws (EmptyResultDataAccessException
            ("Customer not found", 1))

        val result = mockMvc.perform(
            get("/api/customer/search")
                .content(objectMapper.writeValueAsString(searchCustomerRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }
}