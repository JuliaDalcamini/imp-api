package com.julia.imp.auth.refresh

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class RefreshToken(
    @Contextual
    @SerialName("_id")
    val id: ObjectId,
    val refreshToken: String,
    val userId: String
)