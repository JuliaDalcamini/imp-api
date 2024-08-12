package com.julia.imp.auth.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String
) {

    companion object {

        fun of(user: User) = UserResponse(
            id = user.id.toString(),
            firstName = user.firstName,
            lastName = user.lastName
        )
    }
}
