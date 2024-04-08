package com.julia.imp.auth.login

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.julia.imp.auth.JwtParams
import com.julia.imp.auth.user.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.mindrot.jbcrypt.BCrypt
import java.util.*

fun Route.loginRoute() {
    val repository by inject<UserRepository>()

    post("/login") {
        val request = call.receive<LoginRequest>()
        val user = repository.findByEmail(request.email)

        if (user != null && BCrypt.checkpw(request.password, user.password)) {
            val token = JWT.create()
                .withAudience(JwtParams.AUDIENCE)
                .withIssuer(JwtParams.DOMAIN)
                .withClaim("user.email", user.email)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(JwtParams.SECRET))

            call.respond(LoginResponse(token))
        } else {
            call.respond(UnauthorizedResponse())
        }
    }
}