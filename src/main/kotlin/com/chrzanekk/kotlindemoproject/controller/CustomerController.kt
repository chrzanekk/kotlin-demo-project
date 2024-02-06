package com.chrzanekk.kotlindemoproject.controller

import com.chrzanekk.kotlindemoproject.payload.*
import com.chrzanekk.kotlindemoproject.service.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customer")
class CustomerController(var customerService: CustomerService) {

    @PostMapping("/add")
    fun createCustomer(@RequestBody newCustomerRequest: NewCustomerRequest): ResponseEntity<NewCustomerResponse> {
        return ResponseEntity.ok(customerService.createCustomer(newCustomerRequest))
    }

    @GetMapping("/search")
    fun searchCustomerByPersonalNumber(@RequestBody searchCustomerRequest: SearchCustomerRequest)
            : ResponseEntity<SearchCustomerResponse> {
        return ResponseEntity.ok(customerService.findByPersonalNumber(searchCustomerRequest))
    }

    @GetMapping("/filtered")
    fun getCustomers(@RequestBody getCustomersRequest: GetCustomersRequest): ResponseEntity<GetCustomersResponse> {
        return ResponseEntity.ok().body(customerService.findAllCustomers(getCustomersRequest))
    }
}