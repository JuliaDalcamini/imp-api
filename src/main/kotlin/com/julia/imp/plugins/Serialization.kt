package com.julia.imp.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }

    install(RequestValidation)
}