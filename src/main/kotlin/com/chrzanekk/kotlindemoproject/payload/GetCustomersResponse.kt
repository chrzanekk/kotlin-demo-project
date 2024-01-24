package com.chrzanekk.kotlindemoproject.payload

import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO

data class GetCustomersResponse(val customers: List<CustomerDTO>)
