package com.julia.imp.auth

import com.julia.imp.auth.refresh.RefreshTokensRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
    val service by inject<AuthService>()

    post("login") {
        val response = service.login(request = call.receive<LoginRequest>())
        call.respond(response)
    }

    post("refresh_tokens") {
        val tokens = service.refreshTokens(request = call.receive<RefreshTokensRequest>())
        call.respond(tokens)
    }

    post("register") {
        service.register(request = call.receive<RegisterRequest>())
        call.respond(HttpStatusCode.Created)
    }
}