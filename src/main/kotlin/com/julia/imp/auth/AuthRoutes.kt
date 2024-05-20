package com.julia.imp.auth

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
    val service by inject<AuthService>()

    post("/login") {
        val token = service.login(request = call.receive<LoginRequest>())
        call.respond(LoginResponse(token))
    }

    post("/register") {
        service.register(request = call.receive<RegisterRequest>())
        call.respond(HttpStatusCode.Created)
    }
}