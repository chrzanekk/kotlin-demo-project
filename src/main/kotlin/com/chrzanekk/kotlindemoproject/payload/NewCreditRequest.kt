package com.chrzanekk.kotlindemoproject.payload

import com.chrzanekk.kotlindemoproject.service.dto.CreditDTO
import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO

data class NewCreditRequest(val customerDTO: CustomerDTO, val creditDTO: CreditDTO)
