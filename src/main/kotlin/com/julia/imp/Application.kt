package com.julia.imp

import com.julia.imp.plugins.configureDependencyInjection
import com.julia.imp.plugins.configureErrorHandling
import com.julia.imp.plugins.configureHTTP
import com.julia.imp.plugins.configureMonitoring
import com.julia.imp.plugins.configureRouting
import com.julia.imp.plugins.configureSecurity
import com.julia.imp.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDependencyInjection()
    configureSerialization()
    configureSecurity()
    configureMonitoring()
    configureHTTP()
    configureRouting()
    configureErrorHandling()
}
