package com.comercio.repository

import com.comercio.models.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import java.util.*

interface UserRepository : ReactiveMongoRepository<User,String> {
    fun findByUsername(username: String) : Mono<Optional<User>>
    fun existsByUsername(username: String): Mono<Boolean>
    fun existsByEmail(email: String): Mono<Boolean>
}