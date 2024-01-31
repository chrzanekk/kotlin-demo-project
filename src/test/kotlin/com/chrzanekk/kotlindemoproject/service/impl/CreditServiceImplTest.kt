package com.chrzanekk.kotlindemoproject.service.impl

import com.chrzanekk.kotlindemoproject.domain.Credit
import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.payload.NewCreditRequest
import com.chrzanekk.kotlindemoproject.payload.NewCustomerRequest
import com.chrzanekk.kotlindemoproject.payload.SearchCustomerRequest
import com.chrzanekk.kotlindemoproject.repository.CreditRepository
import com.chrzanekk.kotlindemoproject.service.CustomerService
import com.chrzanekk.kotlindemoproject.service.dto.CreditDTO
import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.dao.EmptyResultDataAccessException
import java.math.BigDecimal

class CreditServiceImplTest {
    private val customerService: CustomerService = mockk()
    private val creditRepository: CreditRepository = mockk()
    private val creditServiceImpl = CreditServiceImpl(customerService, creditRepository)

    @Test
    fun whenCreateCredit_ShouldFindCustomer_AndSaveNewCredit() {
        //given
        val savedCustomer = Customer(1L, "John", "Doe", "80808080")
        val savedCustomerDTO =
            CustomerDTO(savedCustomer.id, savedCustomer.firstName, savedCustomer.lastName, savedCustomer.personalNumber)

        val requestCredit = Credit(0L, "NewCredit", savedCustomerDTO.id, BigDecimal.valueOf(10))
        val requestCreditDTO = CreditDTO(0L, "NewCredit", 0L, BigDecimal.valueOf(10))
        val requestCreditCustomerDTO = CustomerDTO(
            0L, savedCustomer.firstName, savedCustomer.lastName, savedCustomer.personalNumber
        )

        val responseCredit = Credit(1L, "NewCredit", savedCustomerDTO.id, BigDecimal.valueOf(10))
        val responseCreditDTO = CreditDTO(1L, "NewCredit", savedCustomerDTO.id, BigDecimal.valueOf(10))
        every { customerService.findByPersonalNumber(SearchCustomerRequest(savedCustomerDTO.personalNumber)) } returns savedCustomerDTO
        every { creditRepository.save(requestCredit) } returns responseCredit

        //then
        val result = creditServiceImpl.createCredit(NewCreditRequest(requestCreditCustomerDTO, requestCreditDTO))

        //then
        verify(exactly = 1) { creditRepository.save(requestCredit) }
        verify(exactly = 1) { customerService.findByPersonalNumber(SearchCustomerRequest(savedCustomer.personalNumber)) }

        assertEquals(responseCreditDTO.id, result.creditId)
    }

    @Test
    fun whenCreateCredit_ShouldNotFindCustomer_AndCreateNewCustomer_AndSaveNewCredit() {
        //given
        val requestCreditDTO = CreditDTO(0L, "NewCredit", 0L, BigDecimal.valueOf(10))
        val requestCreditCustomerDTO = CustomerDTO(0L, "John", "Doe", "808080")

        val searchCustomerRequest = SearchCustomerRequest(requestCreditCustomerDTO.personalNumber)
        val newCustomerRequest = NewCustomerRequest(
            requestCreditCustomerDTO.firstName, requestCreditCustomerDTO
                .lastName, requestCreditCustomerDTO.personalNumber
        )
        val savedCreditCustomerDTO = CustomerDTO(1L, "John", "Doe", "808080")

        val requestCredit =
            Credit(0L, requestCreditDTO.creditName, savedCreditCustomerDTO.id, requestCreditDTO.creditValue)
        val responseCredit = Credit(1L, "NewCredit", savedCreditCustomerDTO.id, BigDecimal.valueOf(10))
        val responseCreditDTO = CreditDTO(1L, "NewCredit", savedCreditCustomerDTO.id, BigDecimal.valueOf(10))

        every { customerService.findByPersonalNumber(searchCustomerRequest) } throws
                (EmptyResultDataAccessException("Customer not found", 1))
        every { customerService.createCustomer(newCustomerRequest) } returns savedCreditCustomerDTO
        every { creditRepository.save(requestCredit) } returns responseCredit

        //then
        val result = creditServiceImpl.createCredit(NewCreditRequest(requestCreditCustomerDTO, requestCreditDTO))

        //then
        verify(exactly = 1) { creditRepository.save(requestCredit) }
        verify(exactly = 1) { customerService.createCustomer(newCustomerRequest) }
        verify(exactly = 1) { customerService.findByPersonalNumber(searchCustomerRequest) }

        assertEquals(responseCreditDTO.id, result.creditId)
    }
}