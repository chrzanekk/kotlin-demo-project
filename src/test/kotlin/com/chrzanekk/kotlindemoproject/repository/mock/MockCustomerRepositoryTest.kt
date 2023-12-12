package com.chrzanekk.kotlindemoproject.repository.mock

import com.chrzanekk.kotlindemoproject.domain.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MockCustomerRepositoryTest {

    private val mockCustomerRepository = MockCustomerRepository()


    @Test
    fun `should return customer when personalNumber is correct`() {
        //given
        val personalNumber: String = "858585"

        //when
        val customer: Customer = mockCustomerRepository.findByPersonalNumber(personalNumber)

        //then
        Assertions.assertThat(customer.personalNumber).isEqualTo(personalNumber)

    }

    @Test
    fun `should throw exception when string is empty`() {
        //given
        val personalNumber: String = ""

        assertThrows<RuntimeException> { mockCustomerRepository.findByPersonalNumber(personalNumber) }

    }
    @Test
    fun `should throw exception when string is incorrect`() {
        //given
        val personalNumber: String = "787878"

        assertThrows<RuntimeException> { mockCustomerRepository.findByPersonalNumber(personalNumber) }

    }
}