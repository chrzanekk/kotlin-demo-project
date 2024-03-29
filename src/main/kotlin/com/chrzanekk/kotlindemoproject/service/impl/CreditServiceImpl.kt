package com.chrzanekk.kotlindemoproject.service.impl

import com.chrzanekk.kotlindemoproject.domain.Credit
import com.chrzanekk.kotlindemoproject.payload.*
import com.chrzanekk.kotlindemoproject.repository.CreditRepository
import com.chrzanekk.kotlindemoproject.service.CreditService
import com.chrzanekk.kotlindemoproject.service.CustomerService
import com.chrzanekk.kotlindemoproject.service.dto.CreditDTO
import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO
import jakarta.transaction.Transactional
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service

@Service
class CreditServiceImpl(
    private val customerService: CustomerService,
    private val creditRepository: CreditRepository
) : CreditService {


    @Transactional
    override fun createCredit(newCreditRequest: NewCreditRequest): NewCreditResponse {
        val customerDTO: CustomerDTO = try {
            val searchCustomerResponse = customerService.findByPersonalNumber(
                SearchCustomerRequest(
                    newCreditRequest.customerDTO.personalNumber
                )
            )
            CustomerDTO(
                searchCustomerResponse.id, searchCustomerResponse.firstName, searchCustomerResponse.lastName,
                searchCustomerResponse.personalNumber
            )
        } catch (e: EmptyResultDataAccessException) {
            val newCustomerResponse = customerService.createCustomer(
                NewCustomerRequest(
                    newCreditRequest.customerDTO
                        .firstName,
                    newCreditRequest.customerDTO.lastName, newCreditRequest.customerDTO.personalNumber
                )
            )
            CustomerDTO(
                newCustomerResponse.id, newCustomerResponse.firstName, newCustomerResponse.lastName,
                newCustomerResponse.personalNumber
            )
        }

        val credit = Credit(
            0L,
            newCreditRequest.creditDTO.creditName,
            customerDTO.id,
            newCreditRequest.creditDTO.creditValue
        )
        return NewCreditResponse(creditRepository.save(credit).id)
    }

    private fun convertEntityToDTO(credits: List<Credit>): List<CreditDTO> {
        return credits.map { credit: Credit ->
            CreditDTO(
                credit.id,
                credit.creditName,
                credit.customerId,
                credit.creditValue
            )
        }
    }

    @Transactional
    override fun findAll(): GetCreditsResponse {
        val creditDTOList = convertEntityToDTO(creditRepository.findAll())
        val customerIds: List<Long> = creditDTOList.map { it.customerId }.distinct()
        val listOfCustomers = customerService.findAllCustomers(GetCustomersRequest(customerIds))
        val listOfCreditsResponse = mutableListOf<GetCreditResponse>()
        for (credit in creditDTOList) {
            for (customer in listOfCustomers.customers) {
                if (credit.customerId == customer.id) {
                    listOfCreditsResponse.add(GetCreditResponse(customer, credit))
                }
            }
        }
        return GetCreditsResponse(listOfCreditsResponse)
    }
}