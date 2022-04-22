package com.comercio.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "modules")
data class Module(
        @Id
        var id: String,
        var name: String,
)
