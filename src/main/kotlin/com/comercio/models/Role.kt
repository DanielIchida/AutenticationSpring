package com.comercio.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "roles")
data class Role(
        @Id
        var id: String,
        var name: ERole)
