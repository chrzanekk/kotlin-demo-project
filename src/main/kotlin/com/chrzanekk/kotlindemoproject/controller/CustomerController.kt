package com.chrzanekk.kotlindemoproject.controller

import com.chrzanekk.kotlindemoproject.payload.*
import com.chrzanekk.kotlindemoproject.service.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customer")
class CustomerController(var customerService: CustomerService) {

    @PostMapping("/")
    fun createCustomer(@RequestBody newCustomerRequest: NewCustomerRequest): ResponseEntity<NewCustomerResponse> {
        return ResponseEntity.ok(customerService.createCustomer(newCustomerRequest))
    }

    @GetMapping("/search")
    fun searchCustomerByPersonalNumber(@RequestBody searchCustomerRequest: SearchCustomerRequest)
            : ResponseEntity<SearchCustomerResponse> {
        return ResponseEntity.ok(customerService.findByPersonalNumber(searchCustomerRequest))
    }

    @GetMapping("/filtered")
    fun getCustomers(@RequestBody getCustomersRequest: GetCustomersRequest) : ResponseEntity<GetCustomersResponse> {
        return ResponseEntity.ok(customerService.findAllCustomers(getCustomersRequest))
    }
}