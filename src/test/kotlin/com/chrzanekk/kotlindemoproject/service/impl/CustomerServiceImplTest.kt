package com.chrzanekk.kotlindemoproject.service.impl

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.repository.mock.MockCustomerRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class CustomerServiceImplTest {
    private val mockCustomerRepository: MockCustomerRepository = mockk()
    private val customerServiceImpl = CustomerServiceImpl(mockCustomerRepository)

    @Test
    fun `should call repository to retrieve customer`() {
        //given
        val personalNumber: String = "838383"
        every { mockCustomerRepository.findByPersonalNumber(personalNumber) } returns Customer(1L, "John", "Doe", "838383")

        //when
        val customer: Customer = customerServiceImpl.findByPersonalNumber(personalNumber)

        //then
        verify(exactly = 1) { mockCustomerRepository.findByPersonalNumber(personalNumber) }
        Assertions.assertThat(customer.personalNumber).isEqualTo(personalNumber)
        Assertions.assertThat(customer.firstName).isNotEmpty()
        Assertions.assertThat(customer.lastName).isNotEmpty()

    }

}