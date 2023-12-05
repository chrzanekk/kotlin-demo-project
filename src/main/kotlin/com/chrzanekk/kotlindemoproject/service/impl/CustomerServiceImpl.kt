package com.chrzanekk.kotlindemoproject.service.impl

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import com.chrzanekk.kotlindemoproject.service.CustomerService
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl : CustomerService {


    override fun findByPersonalNumber(personalNumber: String): Customer {
        TODO("Not yet implemented")
    }
}