package com.chrzanekk.kotlindemoproject.payload

data class NewCustomerRequest(
    val firstName: String,
    val lastName: String,
    val personalNumber: String
)
