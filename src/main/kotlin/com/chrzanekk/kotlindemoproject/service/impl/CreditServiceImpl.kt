package com.chrzanekk.kotlindemoproject.service.impl

import com.chrzanekk.kotlindemoproject.domain.Credit
import com.chrzanekk.kotlindemoproject.payload.GetCreditResponse
import com.chrzanekk.kotlindemoproject.payload.NewCreditRequest
import com.chrzanekk.kotlindemoproject.payload.NewCreditResponse
import com.chrzanekk.kotlindemoproject.payload.NewCustomerRequest
import com.chrzanekk.kotlindemoproject.repository.CreditRepository
import com.chrzanekk.kotlindemoproject.service.CreditService
import com.chrzanekk.kotlindemoproject.service.CustomerService

class CreditServiceImpl(
    private val customerService: CustomerService,
    private val creditRepository: CreditRepository
) : CreditService {
    override fun createCredit(newCreditRequest: NewCreditRequest): NewCreditResponse {
        val customerDTO = getCustomerOrCreate(newCreditRequest)
        val credit: Credit? = customerDTO.id?.let {
             Credit(
                null, newCreditRequest.creditDTO.creditName, it,
                newCreditRequest.creditDTO.creditValue
            )
        }
        val savedCredit = creditRepository.save(credit)
        savedCredit.id?.let { return NewCreditResponse(it) } ?: run {
            throw NoSuchElementException("Failed to save new credit")
        }

    }

    private fun getCustomerOrCreate(newCreditRequest: NewCreditRequest) =
        customerService.findByPersonalNumber(
            newCustomerRequest = NewCustomerRequest(
                newCreditRequest.customerDTO.firstName,
                newCreditRequest.customerDTO.lastName,
                newCreditRequest.customerDTO.personalNumber
            )
        )

    override fun findAll(): GetCreditResponse {
        TODO("Not yet implemented")
    }
}