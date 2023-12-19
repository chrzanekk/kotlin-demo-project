package com.chrzanekk.kotlindemoproject.payload

import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO

data class GetCustomerResponse(val customers: List<CustomerDTO>)
