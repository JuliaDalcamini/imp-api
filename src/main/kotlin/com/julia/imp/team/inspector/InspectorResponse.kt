package com.julia.imp.team.inspector

import com.julia.imp.auth.user.User
import kotlinx.serialization.Serializable

@Serializable
data class InspectorResponse(
    val id: String,
    val firstName: String,
    val lastName: String
) {

    companion object {

        fun of(user: User) = InspectorResponse(
            id = user.id.toString(),
            firstName = user.firstName,
            lastName = user.lastName
        )
    }
}