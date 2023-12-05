package com.chrzanekk.kotlindemoproject.service

import com.chrzanekk.kotlindemoproject.domain.Customer

interface CustomerService {

    fun findByPersonalNumber(personalNumber: String): Customer
}