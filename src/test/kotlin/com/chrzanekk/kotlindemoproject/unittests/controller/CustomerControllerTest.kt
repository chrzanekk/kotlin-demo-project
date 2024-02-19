package com.chrzanekk.kotlindemoproject.unittests.controller

import com.chrzanekk.kotlindemoproject.controller.CustomerController
import com.chrzanekk.kotlindemoproject.payload.*
import com.chrzanekk.kotlindemoproject.service.CustomerService
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
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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

        mockMvc.perform(
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

    @Test
    fun requestToFindCustomerShouldThrowExceptionForNotFound() {
        val personalNumber = "808080"
        val searchCustomerRequest = SearchCustomerRequest(personalNumber)

        every { service.findByPersonalNumber(searchCustomerRequest) } throws (EmptyResultDataAccessException
            ("Customer not found", 1))

        mockMvc.perform(
            get("/api/customer/search")
                .content(objectMapper.writeValueAsString(searchCustomerRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun requestToCreateCustomerShouldTReturnCustomerWithId() {
        val expectedResponse = NewCustomerResponse(1L, "John", "Doe", "808080")
        val createCustomerRequest = NewCustomerRequest("John", "Doe", "808080")

        every { service.createCustomer(createCustomerRequest) } returns expectedResponse

        mockMvc.perform(
            post("/api/customer/add")
                .content(objectMapper.writeValueAsString(createCustomerRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(expectedResponse.id))
            .andExpect(jsonPath("$.firstName").value(expectedResponse.firstName))
            .andExpect(jsonPath("$.lastName").value(expectedResponse.lastName))
            .andExpect(jsonPath("$.personalNumber").value(expectedResponse.personalNumber))
    }

    @Test
    fun requestToFindAllCustomersShouldTReturnResponseWithListOfCustomers() {
        val firstCustomerDTO = CustomerDTO(1L, "John", "Doe", "808080")
        val secondCustomerDTO = CustomerDTO(2L, "Jimmy", "Black", "828282")
        val expectedResponse = GetCustomersResponse(mutableListOf(firstCustomerDTO, secondCustomerDTO))
        val findCustomersRequest = GetCustomersRequest(mutableListOf(1L, 2L))

        every { service.findAllCustomers(findCustomersRequest) } returns expectedResponse

        mockMvc.perform(
            get("/api/customer/filtered")
                .content(objectMapper.writeValueAsString(findCustomersRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.customers.[0].id").value(firstCustomerDTO.id.toInt()))
            .andExpect(jsonPath("$.customers.[0].firstName").value(firstCustomerDTO.firstName))
            .andExpect(jsonPath("$.customers.[0].lastName").value(firstCustomerDTO.lastName))
            .andExpect(jsonPath("$.customers.[0].personalNumber").value(firstCustomerDTO.personalNumber))
            .andExpect(jsonPath("$.customers.[1].id").value(secondCustomerDTO.id.toInt()))
            .andExpect(jsonPath("$.customers.[1].firstName").value(secondCustomerDTO.firstName))
            .andExpect(jsonPath("$.customers.[1].lastName").value(secondCustomerDTO.lastName))
            .andExpect(jsonPath("$.customers.[1].personalNumber").value(secondCustomerDTO.personalNumber))
    }

    @Test
    fun requestToFindAllCustomersShouldTReturnEmptyCollection() {
        val expectedResponse = GetCustomersResponse(mutableListOf())
        val findCustomersRequest = GetCustomersRequest(mutableListOf(1L, 2L))

        every { service.findAllCustomers(findCustomersRequest) } returns expectedResponse

        mockMvc.perform(
            get("/api/customer/filtered")
                .content(objectMapper.writeValueAsString(findCustomersRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.customers").isEmpty)

    }
}