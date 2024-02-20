package com.chrzanekk.kotlindemoproject.integrationtests

import com.chrzanekk.kotlindemoproject.repository.CreditRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreditIntegrationTest : AbstractTestconteinersIntegrationTest() {

    @Autowired
    private lateinit var webTestClient: WebTestClient
    @Autowired
    private lateinit var creditRepository: CreditRepository



}