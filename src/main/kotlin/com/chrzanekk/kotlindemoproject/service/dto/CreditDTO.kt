package com.chrzanekk.kotlindemoproject.service.dto

import java.math.BigDecimal

data class CreditDTO(
     val id: Long,
     val creditName: String,
     val customerId: Long,
     val creditValue: BigDecimal
) {}