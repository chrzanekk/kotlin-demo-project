package com.chrzanekk.kotlindemoproject.payload

import com.chrzanekk.kotlindemoproject.service.dto.CustomerDTO

data class SearchCustomerResponse(val searchResult: List<CustomerDTO>)
