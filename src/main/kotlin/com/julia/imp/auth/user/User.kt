package com.julia.imp.auth.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class User(
    @Contextual
    @SerialName("_id")
    val id: ObjectId,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)
