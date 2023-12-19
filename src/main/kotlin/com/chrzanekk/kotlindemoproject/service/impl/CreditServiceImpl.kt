package com.chrzanekk.kotlindemoproject.service.impl

import com.chrzanekk.kotlindemoproject.payload.GetCreditResponse
import com.chrzanekk.kotlindemoproject.payload.NewCreditRequest
import com.chrzanekk.kotlindemoproject.payload.NewCreditResponse
import com.chrzanekk.kotlindemoproject.service.CreditService

class CreditServiceImpl :CreditService {
    override fun createCredit(newCreditRequest: NewCreditRequest): NewCreditResponse {
        TODO("Not yet implemented")
    }

    override fun findAll(): GetCreditResponse {
        TODO("Not yet implemented")
    }
}