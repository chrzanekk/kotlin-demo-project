package com.chrzanekk.kotlindemoproject.service.impl

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.payload.*
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import com.chrzanekk.kotlindemoproject.service.CustomerService
import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO
import jakarta.transaction.Transactional
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository
) : CustomerService {

    @Transactional(dontRollbackOn = [EmptyResultDataAccessException::class])
    override fun findByPersonalNumber(searchCustomerRequest: SearchCustomerRequest): SearchCustomerResponse {
        val customer: Customer = customerRepository.findByPersonalNumber(searchCustomerRequest.personalNumber)
            ?: throw EmptyResultDataAccessException("Customer not found", 1)
        return SearchCustomerResponse(customer.id, customer.firstName, customer.lastName, customer
            .personalNumber)
    }


    @Transactional
    override fun createCustomer(newCustomerRequest: NewCustomerRequest): NewCustomerResponse {

        val customer = customerRepository.save(
            Customer(
                0, newCustomerRequest.firstName, newCustomerRequest.lastName,
                newCustomerRequest.personalNumber
            )
        )
        return NewCustomerResponse(customer.id, customer.firstName, customer.lastName, customer
            .personalNumber)
    }
    @Transactional
    override fun findAllCustomers(customerIds: GetCustomersRequest): GetCustomersResponse {
        val customerList = customerRepository.findAllById(customerIds.customerIds)
        val customerDTOS: ArrayList<CustomerDTO> = ArrayList()
        customerList.forEach { customer ->
            customerDTOS.add(CustomerDTO(customer.id, customer.firstName, customer.lastName, customer.personalNumber))
        }
        return GetCustomersResponse(customerDTOS)
    }
}
