package com.comercio.config.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.naming.MalformedLinkException

@Component
class JwtUtils {

    @Value("\${comercio.app.jwtSecret}")
    private val jwtSecret: String = ""

    @Value("\${comercio.app.jwtExpirationMs}")
    private val jwtExpirationMs = 0

    private val logger: Logger = LoggerFactory.getLogger(JwtUtils::class.java)


    fun generateJwtToken(authenticacion: Authentication) : String{

        val userPrincipal = authenticacion.principal as UserDetailsImpl
        return Jwts.builder()
                .setSubject(userPrincipal.username)
                .setIssuedAt(Date())
                .setExpiration(Date((Date()).time + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact()

    }

    fun getUserNameFromJwtToken(token: String) : String{
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token).body.subject
    }

    fun validateJwtToken(authToken: String) : Boolean{
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(authToken);
            return true
        }catch (e: SignatureException){
            logger.error("Invalid JWT signature: {}", e.message);
        }catch (e: MalformedLinkException){
            logger.error("Invalid JWT token: {}", e.message);
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message);
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message);
        }
        return false;
    }
}