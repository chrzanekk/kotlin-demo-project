package com.chrzanekk.kotlindemoproject.service

import com.chrzanekk.kotlindemoproject.payload.GetCreditsResponse
import com.chrzanekk.kotlindemoproject.payload.NewCreditRequest
import com.chrzanekk.kotlindemoproject.payload.NewCreditResponse

interface CreditService {

    fun createCredit(newCreditRequest: NewCreditRequest) : NewCreditResponse
    fun findAll() : GetCreditsResponse
}