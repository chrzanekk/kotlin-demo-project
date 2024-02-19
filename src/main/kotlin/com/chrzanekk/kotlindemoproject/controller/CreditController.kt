package com.chrzanekk.kotlindemoproject.controller

import com.chrzanekk.kotlindemoproject.payload.GetCreditsResponse
import com.chrzanekk.kotlindemoproject.payload.NewCreditRequest
import com.chrzanekk.kotlindemoproject.payload.NewCreditResponse
import com.chrzanekk.kotlindemoproject.service.CreditService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/credit")
class CreditController(var creditService: CreditService) {

    @PostMapping("/create")
    fun createCredit(@RequestBody newCreditRequest: NewCreditRequest): ResponseEntity<NewCreditResponse>{
        return ResponseEntity.ok(creditService.createCredit(newCreditRequest))
    }


    @GetMapping("/getCredits")
    fun getCredits() : ResponseEntity<GetCreditsResponse> {
        return ResponseEntity.ok(creditService.findAll())
    }
}