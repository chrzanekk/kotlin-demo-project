package com.chrzanekk.kotlindemoproject.repository.mock

import com.chrzanekk.kotlindemoproject.domain.Credit
import com.chrzanekk.kotlindemoproject.repository.CreditRepository
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.query.FluentQuery
import java.math.BigDecimal
import java.util.*
import java.util.function.Function

class MockCreditRepository : CreditRepository {

    private val credits = mutableListOf(
        Credit(1L, "Credit 1", 1L, BigDecimal.valueOf(50000)),
        Credit(2L, "Credit 2", 2L, BigDecimal.valueOf(100000)),
        Credit(3L, "Credit 3", 3L, BigDecimal.valueOf(150000))
    )

    override fun save(credit: Credit): Credit {
        val lastEntityId = credits.maxWith(Comparator.comparingLong { it.id }).id
        val newId = lastEntityId + 1L;
        credits.add(Credit(newId, credit.creditName, credit.customerId, credit.creditValue))
        return credits.find { c -> c.id == newId }
            ?: throw RuntimeException()
    }


override fun <S : Credit?> save(entity: S & Any): S & Any {
    TODO("Not yet implemented")

}

override fun <S : Credit?> saveAll(entities: MutableIterable<S>): MutableList<S> {
    TODO("Not yet implemented")
}

override fun existsById(id: Long): Boolean {
    TODO("Not yet implemented")
}

override fun findById(id: Long): Optional<Credit> {
    TODO("Not yet implemented")
}

override fun <S : Credit?> findAll(example: Example<S>): MutableList<S> {
    TODO("Not yet implemented")
}

override fun <S : Credit?> findAll(example: Example<S>, sort: Sort): MutableList<S> {
    TODO("Not yet implemented")
}

override fun findAll(): MutableList<Credit> {
    return credits;
}

override fun findAll(sort: Sort): MutableList<Credit> {
    TODO("Not yet implemented")
}

override fun findAll(pageable: Pageable): Page<Credit> {
    TODO("Not yet implemented")
}

override fun <S : Credit?> findAll(example: Example<S>, pageable: Pageable): Page<S> {
    TODO("Not yet implemented")
}

override fun findAllById(ids: MutableIterable<Long>): MutableList<Credit> {
    TODO("Not yet implemented")
}

override fun count(): Long {
    TODO("Not yet implemented")
}

override fun <S : Credit?> count(example: Example<S>): Long {
    TODO("Not yet implemented")
}

override fun delete(entity: Credit) {
    TODO("Not yet implemented")
}

override fun deleteAllById(ids: MutableIterable<Long>) {
    TODO("Not yet implemented")
}

override fun deleteAll(entities: MutableIterable<Credit>) {
    TODO("Not yet implemented")
}

override fun deleteAll() {
    TODO("Not yet implemented")
}

override fun <S : Credit?> findOne(example: Example<S>): Optional<S> {
    TODO("Not yet implemented")
}

override fun <S : Credit?> exists(example: Example<S>): Boolean {
    TODO("Not yet implemented")
}

override fun <S : Credit?, R : Any?> findBy(
    example: Example<S>,
    queryFunction: Function<FluentQuery.FetchableFluentQuery<S>, R>
): R & Any {
    TODO("Not yet implemented")
}

override fun flush() {
    TODO("Not yet implemented")
}

override fun <S : Credit?> saveAndFlush(entity: S & Any): S & Any {
    TODO("Not yet implemented")
}

override fun <S : Credit?> saveAllAndFlush(entities: MutableIterable<S>): MutableList<S> {
    TODO("Not yet implemented")
}

override fun deleteAllInBatch(entities: MutableIterable<Credit>) {
    TODO("Not yet implemented")
}

override fun deleteAllInBatch() {
    TODO("Not yet implemented")
}

override fun deleteAllByIdInBatch(ids: MutableIterable<Long>) {
    TODO("Not yet implemented")
}

override fun getReferenceById(id: Long): Credit {
    TODO("Not yet implemented")
}

override fun getById(id: Long): Credit {
    TODO("Not yet implemented")
}

override fun getOne(id: Long): Credit {
    TODO("Not yet implemented")
}

override fun deleteById(id: Long) {
    TODO("Not yet implemented")
}
}