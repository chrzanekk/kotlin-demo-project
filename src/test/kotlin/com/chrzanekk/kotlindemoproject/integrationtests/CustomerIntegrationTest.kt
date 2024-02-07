package com.chrzanekk.kotlindemoproject.integrationtests

import com.chrzanekk.kotlindemoproject.payload.SearchCustomerRequest
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.json.JSONArray
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

class CustomerIntegrationTest : BaseIntegrationTest() {

    private lateinit var mockMvc: MockMvc
    private val objectMapper = jacksonObjectMapper()


    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun init() {
        cleanDB()
        CustomerFixture(customerRepository).addMultipleCustomers()
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun shouldFindCustomerByPersonalNumber() {
        val searchCustomerRequest = SearchCustomerRequest("808080")
        val request = MockMvcRequestBuilders.get("/search").content(objectMapper.writeValueAsString(searchCustomerRequest))
        val response = mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk)
        val jsonArray = JSONArray(response.andReturn().response.contentAsString)

    }

}