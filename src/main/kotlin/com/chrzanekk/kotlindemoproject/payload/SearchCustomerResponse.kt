package com.chrzanekk.kotlindemoproject.payload

data class SearchCustomerResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val personalNumber: String
)
