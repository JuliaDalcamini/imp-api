package com.julia.imp.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.julia.imp.auth.JwtParams
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    authentication {
        jwt {
            realm = JwtParams.REALM

            verifier(
                JWT
                    .require(Algorithm.HMAC256(JwtParams.SECRET))
                    .withAudience(JwtParams.AUDIENCE)
                    .withIssuer(JwtParams.DOMAIN)
                    .build()
            )

            validate { credential ->
                if (credential.payload.audience.contains(JwtParams.AUDIENCE)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
