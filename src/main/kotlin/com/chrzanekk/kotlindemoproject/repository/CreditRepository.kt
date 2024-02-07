package com.chrzanekk.kotlindemoproject.repository

import com.chrzanekk.kotlindemoproject.domain.Credit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CreditRepository : JpaRepository<Credit, Long> {

}