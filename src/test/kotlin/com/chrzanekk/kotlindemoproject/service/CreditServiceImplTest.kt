package com.chrzanekk.kotlindemoproject.service

import com.chrzanekk.kotlindemoproject.domain.Credit
import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.payload.*
import com.chrzanekk.kotlindemoproject.repository.CreditRepository
import com.chrzanekk.kotlindemoproject.service.CustomerService
import com.chrzanekk.kotlindemoproject.service.dto.CreditDTO
import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO
import com.chrzanekk.kotlindemoproject.service.impl.CreditServiceImpl
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
        val newCustomerResponse =
            SearchCustomerResponse(savedCustomer.id, savedCustomer.firstName, savedCustomer.lastName, savedCustomer.personalNumber)

        val requestCredit = Credit(0L, "NewCredit", newCustomerResponse.id, BigDecimal.valueOf(10))
        val requestCreditDTO = CreditDTO(0L, "NewCredit", 0L, BigDecimal.valueOf(10))
        val requestCreditCustomerDTO = CustomerDTO(
            0L, savedCustomer.firstName, savedCustomer.lastName, savedCustomer.personalNumber
        )

        val responseCredit = Credit(1L, "NewCredit", newCustomerResponse.id, BigDecimal.valueOf(10))
        val responseCreditDTO = CreditDTO(1L, "NewCredit", newCustomerResponse.id, BigDecimal.valueOf(10))
        every { customerService.findByPersonalNumber(SearchCustomerRequest(newCustomerResponse.personalNumber)) } returns newCustomerResponse
        every { creditRepository.save(requestCredit) } returns responseCredit

        //when
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
        val savedCreditCustomerDTO = NewCustomerResponse(1L, "John", "Doe", "808080")

        val requestCredit =
            Credit(0L, requestCreditDTO.creditName, savedCreditCustomerDTO.id, requestCreditDTO.creditValue)
        val responseCredit = Credit(1L, "NewCredit", savedCreditCustomerDTO.id, BigDecimal.valueOf(10))
        val responseCreditDTO = CreditDTO(1L, "NewCredit", savedCreditCustomerDTO.id, BigDecimal.valueOf(10))

        every { customerService.findByPersonalNumber(searchCustomerRequest) } throws
                (EmptyResultDataAccessException("Customer not found", 1))
        every { customerService.createCustomer(newCustomerRequest) } returns savedCreditCustomerDTO
        every { creditRepository.save(requestCredit) } returns responseCredit

        //when
        val result = creditServiceImpl.createCredit(NewCreditRequest(requestCreditCustomerDTO, requestCreditDTO))

        //then
        verify(exactly = 1) { creditRepository.save(requestCredit) }
        verify(exactly = 1) { customerService.createCustomer(newCustomerRequest) }
        verify(exactly = 1) { customerService.findByPersonalNumber(searchCustomerRequest) }

        assertEquals(responseCreditDTO.id, result.creditId)
    }

    @Test
    fun whenGetAllCredits_shouldReturnListOfResponseWithCreditAndCustomerDTO() {
        //given
        val firstCustomerDTO = CustomerDTO(1L, "John", "Doe", "808080")
        val secondCustomerDTO = CustomerDTO(2L, "Jimmy", "Black", "818181")
        val customerResponse = GetCustomersResponse(mutableListOf(firstCustomerDTO, secondCustomerDTO))

        val firstCredit = Credit(1L, "NewCredit", 1L, BigDecimal.valueOf(110))
        val secondCredit = Credit(2L, "NewCredit2", 2L, BigDecimal.valueOf(220))
        val creditResponseFromDB = mutableListOf(firstCredit, secondCredit)

        val getAllCustomers = GetCustomersRequest(mutableListOf(firstCustomerDTO.id, secondCustomerDTO.id))

        every { creditRepository.findAll() } returns creditResponseFromDB
        every { customerService.findAllCustomers(getAllCustomers) } returns customerResponse

        //when
        val result = creditServiceImpl.findAll();

        //then
        verify(exactly = 1) { creditRepository.findAll() }
        verify(exactly = 1) { customerService.findAllCustomers(getAllCustomers) }

        assertEquals(result.credits.size, 2)
        assertEquals(result.credits[0].creditDTO.id,1L)
        assertEquals(result.credits[1].creditDTO.id,2L)
        assertEquals(result.credits[0].customerDTO.id,1L)
        assertEquals(result.credits[1].customerDTO.id,2L)
    }

    @Test
    fun whenGetAllCredits_shouldReturnListOfResponseWithCreditAndCustomerDTOButCustomerHaveTwoCredits() {
        //given
        val firstCustomerDTO = CustomerDTO(1L, "John", "Doe", "808080")
        val customerResponse = GetCustomersResponse(mutableListOf(firstCustomerDTO))

        val firstCredit = Credit(1L, "NewCredit", 1L, BigDecimal.valueOf(110))
        val secondCredit = Credit(2L, "NewCredit2", 1L, BigDecimal.valueOf(220))
        val creditResponseFromDB = mutableListOf(firstCredit, secondCredit)

        val getAllCustomers = GetCustomersRequest(mutableListOf(firstCustomerDTO.id))

        every { creditRepository.findAll() } returns creditResponseFromDB
        every { customerService.findAllCustomers(getAllCustomers) } returns customerResponse

        //when
        val result = creditServiceImpl.findAll();

        //then
        verify(exactly = 1) { creditRepository.findAll() }
        verify(exactly = 1) { customerService.findAllCustomers(getAllCustomers) }

        assertEquals(result.credits.size, 2)
        assertEquals(result.credits[0].creditDTO.id,1L)
        assertEquals(result.credits[1].creditDTO.id,2L)
        assertEquals(result.credits[0].customerDTO.id,1L)
        assertEquals(result.credits[1].customerDTO.id,1L)
    }

    @Test
    fun whenGetAllCredits_shouldReturnEmptyList() {
        //given
        val customerResponse = GetCustomersResponse(mutableListOf())

        val creditResponseFromDB = mutableListOf<Credit>()

        val getAllCustomers = GetCustomersRequest(mutableListOf())

        every { creditRepository.findAll() } returns creditResponseFromDB
        every { customerService.findAllCustomers(getAllCustomers) } returns customerResponse

        //when
        val result = creditServiceImpl.findAll();

        //then
        verify(exactly = 1) { creditRepository.findAll() }
        verify(exactly = 1) { customerService.findAllCustomers(getAllCustomers) }

        assertEquals(result.credits.size, 0)
    }
}