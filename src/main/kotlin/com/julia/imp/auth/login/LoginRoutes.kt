package com.julia.imp.auth.login

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.julia.imp.auth.JwtParams
import com.julia.imp.auth.user.UserRepository
import io.ktor.server.application.call
import io.ktor.server.auth.UnauthorizedResponse
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject
import org.mindrot.jbcrypt.BCrypt
import java.util.Date

fun Route.loginRoute() {
    val repository by inject<UserRepository>()

    post("/login") {
        val request = call.receive<LoginRequest>()
        val user = repository.findByEmail(request.email)

        if (user != null && BCrypt.checkpw(request.password, user.password)) {
            val token = JWT.create()
                .withAudience(JwtParams.AUDIENCE)
                .withIssuer(JwtParams.DOMAIN)
                .withClaim("user.id", user.id.toString())
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(JwtParams.SECRET))

            call.respond(LoginResponse(token))
        } else {
            call.respond(UnauthorizedResponse())
        }
    }
}