package com.julia.imp.auth

import com.julia.imp.auth.login.loginRoute
import com.julia.imp.auth.register.registerRoute
import io.ktor.server.routing.*

fun Route.authRoutes() {
    loginRoute()
    registerRoute()
}