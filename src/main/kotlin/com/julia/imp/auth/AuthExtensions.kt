package com.julia.imp.auth

import com.julia.imp.auth.JwtParams.USER_ID_CLAIM
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal

val ApplicationCall.authenticatedUserId: String
    get() = principal<JWTPrincipal>()?.payload?.getClaim(USER_ID_CLAIM)?.asString()
        ?: throw IllegalStateException("The user is not authenticated")