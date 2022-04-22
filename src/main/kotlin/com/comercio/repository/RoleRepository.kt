package com.comercio.repository

import com.comercio.models.ERole
import com.comercio.models.Role
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import java.util.*


interface RoleRepository : ReactiveMongoRepository<Role,String> {
    fun findByName(role: ERole) : Mono<Optional<Role>>
}