package com.julia.imp.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.julia.imp.auth.JwtParams
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

val jwtVerifier: JWTVerifier by lazy {
    JWT
        .require(Algorithm.HMAC256(JwtParams.SECRET))
        .withAudience(JwtParams.AUDIENCE)
        .withIssuer(JwtParams.DOMAIN)
        .build()
}

fun Application.configureSecurity() {
    authentication {
        jwt {
            realm = JwtParams.REALM

            verifier(jwtVerifier)

            validate { credential ->
                if (credential.payload.audience.contains(JwtParams.AUDIENCE)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
