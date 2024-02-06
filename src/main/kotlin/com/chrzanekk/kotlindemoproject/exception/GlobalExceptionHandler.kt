package com.chrzanekk.kotlindemoproject.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ErrorMessageModel> {
        val errorMessage = ErrorMessageModel(
            HttpStatus.BAD_REQUEST.value(),
            ex.message
        )
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<ErrorMessageModel> {
        val errorMessage = ErrorMessageModel(
            ex.statusCode.value(),
            ex.message
        )
        return ResponseEntity(errorMessage, ex.statusCode)
    }

}