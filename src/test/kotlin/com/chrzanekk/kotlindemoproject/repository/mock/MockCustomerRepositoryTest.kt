package com.chrzanekk.kotlindemoproject.repository.mock

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class MockCustomerRepositoryTest {

    private val mockCustomerRepository = MockCustomerRepository()


    @Test
    fun `should should return customer` () {
    //given
    val personalNumber : String = "personalNumber"
    
    //when
    val customer = mockCustomerRepository.findByPersonalNumber(personalNumber)
    
    //then
        Assertions.assertThat(customer.personalNumber).isEqualTo(personalNumber)
    
    }
}