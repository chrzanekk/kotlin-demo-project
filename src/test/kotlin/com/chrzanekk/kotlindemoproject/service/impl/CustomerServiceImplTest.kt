package com.chrzanekk.kotlindemoproject.service.impl

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.payload.GetCustomersRequest
import com.chrzanekk.kotlindemoproject.payload.GetCustomersResponse
import com.chrzanekk.kotlindemoproject.payload.NewCustomerRequest
import com.chrzanekk.kotlindemoproject.payload.SearchCustomerRequest
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CustomerServiceImplTest {
    private val customerRepository: CustomerRepository = mockk()
    private val customerServiceImpl = CustomerServiceImpl(customerRepository)


    @Test
    fun whenFindByPersonalNumber_thenReturnCustomer() {
        //given
        val personalNumber = "838383"
        val customer = Customer(1L, "John", "Doe", personalNumber)
        val customerDTO = CustomerDTO(customer.id, customer.firstName, customer.lastName, customer.personalNumber)
        every { customerRepository.findByPersonalNumber(personalNumber) } returns customer

        //when
        val result = customerServiceImpl.findByPersonalNumber(SearchCustomerRequest(personalNumber))

        //then
        verify(exactly = 1) { customerRepository.findByPersonalNumber(personalNumber) }
        assertEquals(customerDTO, result)
    }

    @Test
    fun whenFindByPersonalNumber_thenThrowException() {
        //GIVEN
        val personalNumber = "838383"
        every { customerRepository.findByPersonalNumber(personalNumber) } throws NoSuchElementException()

        //WHEN
        var exceptionThrown = false
        try {
            customerServiceImpl.findByPersonalNumber(SearchCustomerRequest(personalNumber))
        } catch (exception: NoSuchElementException) {
            exceptionThrown = true
        }
        assertTrue(exceptionThrown)

        //THEN
        verify(exactly = 1) { customerRepository.findByPersonalNumber(personalNumber) }
    }

    @Test
    fun whenCreateCustomer_thenReturnCustomerWithId() {
        //given
        val customerToSave = Customer(0L, "John", "Doe", "838383")
        val savedCustomer = Customer(1L, "John", "Doe", "838383")
        val customerDTO = CustomerDTO(
            savedCustomer.id, savedCustomer.firstName, savedCustomer.lastName, savedCustomer
                .personalNumber
        )
        every { customerRepository.save(customerToSave) } returns savedCustomer

        //when
        val result = customerServiceImpl.createCustomer(NewCustomerRequest("John", "Doe", "838383"))

        //then
        verify(exactly = 1) { customerRepository.save(customerToSave) }
        assertEquals(customerDTO, result)
    }

    @Test
    fun whenFindAllCustomers_thenReturnCustomerList() {
        //given
        val firstSavedCustomer = Customer(1L, "John", "Doe", "838383")
        val secondSavedCustomer = Customer(2L, "Jimmy", "Black", "111111")
        val listOfCustomersToReturn = mutableListOf(firstSavedCustomer, secondSavedCustomer)

        val firstCustomerDTO = CustomerDTO(
            firstSavedCustomer.id, firstSavedCustomer.firstName, firstSavedCustomer.lastName,
            firstSavedCustomer.personalNumber
        )
        val secondCustomerDTO = CustomerDTO(
            secondSavedCustomer.id, secondSavedCustomer.firstName, secondSavedCustomer.lastName,
            secondSavedCustomer.personalNumber
        )
        val listOfCustomerDTOS = mutableListOf(firstCustomerDTO, secondCustomerDTO)
        val getCustomersResponse = GetCustomersResponse(listOfCustomerDTOS)

        val listOfLongs = mutableListOf(1L, 2L)

        val getCustomerRequest = GetCustomersRequest(listOfLongs)
        every { customerRepository.findAllById(listOfLongs) } returns listOfCustomersToReturn

        //when
        val result = customerServiceImpl.findAllCustomers(getCustomerRequest)

        //then
        verify(exactly = 1) { customerRepository.findAllById(listOfLongs) }
        assertEquals(getCustomersResponse, result)
    }

    @Test
    fun whenFindAllCustomers_thenReturnCustomerListWithOneElement() {
        //given
        val firstSavedCustomer = Customer(1L, "John", "Doe", "838383")
        val secondSavedCustomer = Customer(2L, "Jimmy", "Black", "111111")
        val listOfCustomersToReturn = mutableListOf(secondSavedCustomer)

        val firstCustomerDTO = CustomerDTO(
            firstSavedCustomer.id, firstSavedCustomer.firstName, firstSavedCustomer.lastName,
            firstSavedCustomer.personalNumber
        )
        val secondCustomerDTO = CustomerDTO(
            secondSavedCustomer.id, secondSavedCustomer.firstName, secondSavedCustomer.lastName,
            secondSavedCustomer.personalNumber
        )
        val listOfCustomerDTOS = mutableListOf(secondCustomerDTO)
        val getCustomersResponse = GetCustomersResponse(listOfCustomerDTOS)

        val listOfLongs = mutableListOf(2L)

        val getCustomerRequest = GetCustomersRequest(listOfLongs)
        every { customerRepository.findAllById(listOfLongs) } returns listOfCustomersToReturn

        //when
        val result = customerServiceImpl.findAllCustomers(getCustomerRequest)

        //then
        verify(exactly = 1) { customerRepository.findAllById(listOfLongs) }
        assertEquals(getCustomersResponse, result)
        assertEquals(getCustomersResponse.customers.size, 1)
        assertEquals(getCustomersResponse.customers[0].id, 2L)
    }
}