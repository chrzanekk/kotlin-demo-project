package com.chrzanekk.kotlindemoproject.service

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.payload.SearchCustomerRequest

interface CustomerService {

    fun findByPersonalNumber(searchCustomerRequest: SearchCustomerRequest): Customer
}