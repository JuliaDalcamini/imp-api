package com.julia.imp.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.julia.imp.auth.JwtParams.USER_ID_CLAIM
import com.julia.imp.auth.user.User
import com.julia.imp.auth.user.UserRepository
import com.julia.imp.common.networking.error.ConflictError
import com.julia.imp.common.networking.error.UnauthorizedError
import org.bson.types.ObjectId
import org.mindrot.jbcrypt.BCrypt
import java.util.Date

class AuthService(private val repository: UserRepository) {

    suspend fun login(request: LoginRequest): String {
        val user = repository.findByEmail(request.email)

        if (user != null && BCrypt.checkpw(request.password, user.password)) {
            val token = JWT.create()
                .withAudience(JwtParams.AUDIENCE)
                .withIssuer(JwtParams.DOMAIN)
                .withClaim(USER_ID_CLAIM, user.id.toString())
                .withExpiresAt(Date(System.currentTimeMillis() + TOKEN_DURATION))
                .sign(Algorithm.HMAC256(JwtParams.SECRET))

            return token
        } else {
            throw UnauthorizedError("Unable to authenticate")
        }
    }

    suspend fun register(request: RegisterRequest) {
        val existingUser = repository.findByEmail(request.email)

        if (existingUser == null) {
            repository.insert(
                User(
                    id = ObjectId(),
                    firstName = request.firstName,
                    lastName = request.lastName,
                    email = request.email,
                    password = BCrypt.hashpw(request.password, BCrypt.gensalt(PASSWORD_HASHING_ROUNDS))
                )
            )
        } else {
            throw ConflictError("User already exists")
        }
    }

    companion object {
        private const val TOKEN_DURATION = 60000
        private const val PASSWORD_HASHING_ROUNDS = 12
    }
}