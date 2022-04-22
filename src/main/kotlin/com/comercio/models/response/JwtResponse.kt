package com.comercio.models.response

data class JwtResponse(
        var token: String,
        var type: String = "Bearer",
        var id: String,
        var username:  String,
        var email: String,
        var roles: List<String>
)
