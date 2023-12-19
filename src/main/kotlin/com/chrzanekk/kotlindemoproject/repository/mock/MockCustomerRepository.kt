package com.chrzanekk.kotlindemoproject.repository.mock

import com.chrzanekk.kotlindemoproject.domain.Customer
import com.chrzanekk.kotlindemoproject.repository.CustomerRepository
import org.hibernate.ObjectNotFoundException
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.query.FluentQuery
import org.springframework.stereotype.Repository
import java.lang.RuntimeException
import java.util.*
import java.util.function.Function

@Repository
class MockCustomerRepository : CustomerRepository {

    val customers = mutableListOf(
        Customer(1L, "John", "Doe", "838383"),
        Customer(2L, "Jimmy", "Smith", "848484"),
        Customer(3L, "Charlotte", "Firecracker", "858585")
    )

    override fun findByPersonalNumber(personalNumber: String): Customer {
        return customers.find { c -> c.personalNumber == personalNumber }
            ?: throw RuntimeException()
    }

    override fun <S : Customer?> save(entity: S & Any): S & Any {
        TODO("Not yet implemented")
    }

    override fun <S : Customer?> saveAll(entities: MutableIterable<S>): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun <S : Customer?> findAll(example: Example<S>): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun <S : Customer?> findAll(example: Example<S>, sort: Sort): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableList<Customer> {
        TODO("Not yet implemented")
    }

    override fun findAll(sort: Sort): MutableList<Customer> {
        TODO("Not yet implemented")
    }

    override fun findAll(pageable: Pageable): Page<Customer> {
        TODO("Not yet implemented")
    }

    override fun <S : Customer?> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        TODO("Not yet implemented")
    }

    override fun findAllById(ids: MutableIterable<Long>): MutableList<Customer> {
        TODO("Not yet implemented")
    }

    override fun count(): Long {
        TODO("Not yet implemented")
    }

    override fun <S : Customer?> count(example: Example<S>): Long {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Customer) {
        TODO("Not yet implemented")
    }

    override fun deleteAllById(ids: MutableIterable<Long>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll(entities: MutableIterable<Customer>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun <S : Customer?> findOne(example: Example<S>): Optional<S> {
        TODO("Not yet implemented")
    }

    override fun <S : Customer?> exists(example: Example<S>): Boolean {
        TODO("Not yet implemented")
    }

    override fun <S : Customer?, R : Any?> findBy(
        example: Example<S>,
        queryFunction: Function<FluentQuery.FetchableFluentQuery<S>, R>
    ): R & Any {
        TODO("Not yet implemented")
    }

    override fun flush() {
        TODO("Not yet implemented")
    }

    override fun <S : Customer?> saveAndFlush(entity: S & Any): S & Any {
        TODO("Not yet implemented")
    }

    override fun <S : Customer?> saveAllAndFlush(entities: MutableIterable<S>): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun deleteAllInBatch(entities: MutableIterable<Customer>) {
        TODO("Not yet implemented")
    }

    override fun deleteAllInBatch() {
        TODO("Not yet implemented")
    }

    override fun deleteAllByIdInBatch(ids: MutableIterable<Long>) {
        TODO("Not yet implemented")
    }

    override fun getReferenceById(id: Long): Customer {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): Customer {
        TODO("Not yet implemented")
    }

    override fun getOne(id: Long): Customer {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun existsById(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Optional<Customer> {
        TODO("Not yet implemented")
    }
}