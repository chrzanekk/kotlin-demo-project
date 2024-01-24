package com.chrzanekk.kotlindemoproject.service

import com.chrzanekk.kotlindemoproject.payload.GetCustomersRequest
import com.chrzanekk.kotlindemoproject.payload.GetCustomersResponse
import com.chrzanekk.kotlindemoproject.payload.NewCustomerRequest
import com.chrzanekk.kotlindemoproject.payload.SearchCustomerRequest
import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO

interface CustomerService {

    fun findByPersonalNumber(searchCustomerRequest: SearchCustomerRequest): CustomerDTO

    fun createCustomer(newCustomerRequest: NewCustomerRequest): CustomerDTO

    fun findAllCustomers(customerIds: GetCustomersRequest): GetCustomersResponse
}