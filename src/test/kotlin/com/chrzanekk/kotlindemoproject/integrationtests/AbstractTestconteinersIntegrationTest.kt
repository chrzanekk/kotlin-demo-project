package com.chrzanekk.kotlindemoproject.integrationtests

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class AbstractTestconteinersIntegrationTest {
    companion object {

        @Container
        val container = postgres("12") {
            withDatabaseName("db")
            withUsername("user")
            withPassword("password")
            withInitScript("schema.sql")
        }

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
        }
    }

    @Test
    fun containerIsUpAndRunning() {
        Assertions.assertTrue(container.isRunning)
    }
}