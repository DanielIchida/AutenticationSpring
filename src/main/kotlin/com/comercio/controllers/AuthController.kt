package com.comercio.controllers

import com.comercio.config.security.JwtUtils
import com.comercio.config.security.UserDetailsImpl
import com.comercio.models.ERole
import com.comercio.models.Role
import com.comercio.models.User
import com.comercio.models.request.LoginRequest
import com.comercio.models.request.SignupRequest
import com.comercio.models.response.JwtResponse
import com.comercio.models.response.MessageResponse
import com.comercio.repository.RoleRepository
import com.comercio.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import javax.validation.Valid

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
class AuthController {

   @Autowired
   lateinit var authenticationManager: AuthenticationManager

   @Autowired
   lateinit var userRepository: UserRepository

   @Autowired
   lateinit var roleRepository: RoleRepository

   @Autowired
   lateinit var encoder: PasswordEncoder

   @Autowired
   lateinit var jwtUtils: JwtUtils

   @PostMapping("/signin")
   fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest) :  ResponseEntity<Any>{
        val authetication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.username,loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authetication
        val jwt = jwtUtils.generateJwtToken(authetication)
        val userDetails = authetication.principal as UserDetailsImpl
        val roles = userDetails.authorities.stream()
                .map { item -> item.authority }
                .collect(Collectors.toList())
        return ResponseEntity.ok(JwtResponse(
                token = jwt,
                id = userDetails.id,
                username = userDetails.username,
                email = userDetails.email,
                roles = roles
        ))
   }

   @PostMapping("/signup")
   fun registerUser(@Valid @RequestBody signUpRequest: SignupRequest) : ResponseEntity<Any>{
       if(userRepository.existsByUsername(signUpRequest.username).block() == true){
          return ResponseEntity
                  .badRequest()
                  .body(MessageResponse("Error: UserName is already taken"))
       }
       if(userRepository.existsByEmail(signUpRequest.email).block() == true){
           return ResponseEntity
                   .badRequest()
                   .body(MessageResponse("Error: Email is already taken"))
       }
       val user = User(signUpRequest.username,signUpRequest.email,encoder.encode(signUpRequest.password))
       val strRoles = signUpRequest.roles
       val roles = HashSet<Role>()
       if(strRoles == null){
          val userRole = roleRepository.findByName(ERole.ROLE_USER).block()?.orElseThrow {
              RuntimeException("Error: Role is not found.")
          }
          roles.add(userRole!!)
       }else{
           strRoles.forEach {
               when(it){
                   "admin" -> {
                       val adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).block()?.orElseThrow {
                           RuntimeException("Error: Role is not found.")
                       }
                       roles.add(adminRole!!)
                   }
                   "commerce" -> {
                       val commRole = roleRepository.findByName(ERole.ROLE_COMMERCE).block()?.orElseThrow {
                           RuntimeException("Error: Role is not found.")
                       }
                       roles.add(commRole!!)
                   }
               }
           }
       }
       user.roles = roles
       userRepository.save(user)
       return ResponseEntity.ok(MessageResponse("User register successfully"))
   }



}