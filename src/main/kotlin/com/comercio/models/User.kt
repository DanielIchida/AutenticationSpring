package com.comercio.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Document(collection = "users")
data class User(
        @Id
        var id: String,
        @NotBlank
        @Size(max = 50)
        var username: String,
        @NotBlank
        @Size(max = 70)
        @Email
        var email: String,
        @NotBlank
        @Size(max = 120)
        var password: String?,
        @DBRef
        var roles: Set<Role>?,
) {
        constructor(id: String, username: String, email: String) : this(id,username,email,null,null)
}
