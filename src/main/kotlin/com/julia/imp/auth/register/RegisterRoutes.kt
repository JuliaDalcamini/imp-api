package com.julia.imp.auth.register

import com.julia.imp.auth.user.User
import com.julia.imp.auth.user.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import org.mindrot.jbcrypt.BCrypt

fun Route.registerRoute() {
    val repository by inject<UserRepository>()

    post("/register") {
        val request = call.receive<RegisterRequest>()
        val existingUser = repository.findByEmail(request.email)

        if (existingUser == null) {
            repository.insertOne(
                User(
                    id = ObjectId(),
                    firstName = request.firstName,
                    lastName = request.lastName,
                    email = request.email,
                    password = BCrypt.hashpw(request.password, BCrypt.gensalt(12))
                )
            )

            call.respond(HttpStatusCode.Created)
        } else {
            call.respond(HttpStatusCode.Conflict)
        }
    }
}