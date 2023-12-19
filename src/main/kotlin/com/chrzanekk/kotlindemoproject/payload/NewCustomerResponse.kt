package com.chrzanekk.kotlindemoproject.payload

data class NewCustomerResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val personalNumber: String
)
