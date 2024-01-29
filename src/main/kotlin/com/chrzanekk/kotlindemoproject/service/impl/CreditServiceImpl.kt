package com.chrzanekk.kotlindemoproject.service.impl

import com.chrzanekk.kotlindemoproject.domain.Credit
import com.chrzanekk.kotlindemoproject.payload.*
import com.chrzanekk.kotlindemoproject.repository.CreditRepository
import com.chrzanekk.kotlindemoproject.service.CreditService
import com.chrzanekk.kotlindemoproject.service.CustomerService
import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO

class CreditServiceImpl(
    private val customerService: CustomerService,
    private val creditRepository: CreditRepository
) : CreditService {
    override fun createCredit(newCreditRequest: NewCreditRequest): NewCreditResponse {
        val customerDTO = customerService.findByPersonalNumber(
            SearchCustomerRequest(
                newCreditRequest.customerDTO.personalNumber
            )
        )
        if (customerDTO.id != 0L) {
            val credit = Credit(
                0L, newCreditRequest.creditDTO.creditName, customerDTO.id,
                newCreditRequest.creditDTO.creditValue
            )
            return NewCreditResponse(creditRepository.save(credit).id)
        } else {
            val newCustomerDTO: CustomerDTO = customerService.createCustomer(
                NewCustomerRequest(
                    newCreditRequest.customerDTO.firstName,
                    newCreditRequest.customerDTO.lastName,
                    newCreditRequest.customerDTO.personalNumber
                )
            )
            val credit = Credit(
                0L,
                newCreditRequest.creditDTO.creditName,
                newCustomerDTO.id,
                newCreditRequest.creditDTO.creditValue
            )
            return NewCreditResponse(creditRepository.save(credit).id)
        }
    }

    override fun findAll(): GetCreditResponse {
        TODO("Not yet implemented")
    }
}