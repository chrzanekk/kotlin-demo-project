package com.chrzanekk.kotlindemoproject.service

import com.chrzanekk.kotlindemoproject.payload.*
import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO

interface CustomerService {

    fun findByPersonalNumber(searchCustomerRequest: SearchCustomerRequest): SearchCustomerResponse

    fun createCustomer(newCustomerRequest: NewCustomerRequest): NewCustomerResponse

    fun findAllCustomers(customerIds: GetCustomersRequest): GetCustomersResponse
}