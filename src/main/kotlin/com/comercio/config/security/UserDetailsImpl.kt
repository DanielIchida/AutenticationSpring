package com.comercio.config.security

import com.comercio.models.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

class UserDetailsImpl(
        var id: String,
        var email: String,
        private var username: String,
        @JsonIgnore
        private var password: String,
        private var authorities: MutableCollection<out GrantedAuthority>
) : UserDetails {

    companion object{
        fun build(user: User): UserDetailsImpl{
            val authorities = user.roles?.stream()
                    ?.map { role -> SimpleGrantedAuthority(role.name.name) }
                    ?.collect(Collectors.toList())
            return UserDetailsImpl(
                    user.id,
                    user.email,
                    user.username,
                    user.password!!,
                    authorities!!
            )
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }



    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}