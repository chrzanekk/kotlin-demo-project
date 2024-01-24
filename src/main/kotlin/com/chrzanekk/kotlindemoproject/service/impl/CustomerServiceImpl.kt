package com.chrzanekk.kotlindemoproject.service.impl

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.payload.GetCustomersRequest
import com.chrzanekk.kotlindemoproject.payload.GetCustomersResponse
import com.chrzanekk.kotlindemoproject.payload.NewCustomerRequest
import com.chrzanekk.kotlindemoproject.payload.SearchCustomerRequest
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import com.chrzanekk.kotlindemoproject.service.CustomerService
import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository
) : CustomerService {

    override fun findByPersonalNumber(searchCustomerRequest: SearchCustomerRequest): CustomerDTO {
        val customer = customerRepository.findByPersonalNumber(searchCustomerRequest.personalNumber)
        return CustomerDTO(customer.id, customer.firstName, customer.lastName, customer.personalNumber)
    }

    override fun createCustomer(newCustomerRequest: NewCustomerRequest): CustomerDTO {

        val customer = customerRepository.save(
            Customer(
                null, newCustomerRequest.firstName, newCustomerRequest.lastName,
                newCustomerRequest.personalNumber
            )
        )
        return CustomerDTO(customer.id, customer.firstName, customer.lastName, customer.personalNumber)
    }

    override fun findAllCustomers(customerIds: GetCustomersRequest): GetCustomersResponse {
        val customerList = customerRepository.findAllById(customerIds.customerIds)
        val customerDTOS: ArrayList<CustomerDTO> = ArrayList()
        customerList.forEach { customer ->
            customerDTOS.add(CustomerDTO(customer.id, customer.firstName, customer.lastName, customer.personalNumber))
        }
        return GetCustomersResponse(customerDTOS)
    }
}
