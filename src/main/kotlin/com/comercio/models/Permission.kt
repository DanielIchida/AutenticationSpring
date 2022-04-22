package com.comercio.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "permissions")
data class Permission(
    @Id
    var id: String,
    var user: User,
    var module: Module,
    var edit: Boolean,
    var delete: Boolean,
    var insert: Boolean,
    var find: Boolean
)