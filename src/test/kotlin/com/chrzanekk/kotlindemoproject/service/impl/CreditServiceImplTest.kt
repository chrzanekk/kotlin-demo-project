package com.chrzanekk.kotlindemoproject.service.impl

import com.chrzanekk.kotlindemoproject.repository.mock.MockCreditRepository
import com.chrzanekk.kotlindemoproject.repository.mock.MockCustomerRepository
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*

internal class CreditServiceImplTest {
    private val mockCustomerRepository: MockCustomerRepository = mockk()
    private val mockCreditRepository: MockCreditRepository = mockk()
    private val customerServiceImpl = CustomerServiceImpl(mockCustomerRepository)
    private val creditServiceImpl = CreditServiceImpl(customerServiceImpl,mockCreditRepository)


}