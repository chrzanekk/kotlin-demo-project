package com.chrzanekk.kotlindemoproject.service

import com.chrzanekk.kotlindemoproject.payload.NewCreditRequest

interface CreditService {

    fun createCredit(newCreditRequest: NewCreditRequest)
    fun findAll()
}