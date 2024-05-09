package com.julia.imp.teammember

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class TeamMember(
    @Contextual
    @SerialName("_id")
    val id: ObjectId,

    val userId: String,
    val teamId: String,
    val role: Role
) {

    val isAdmin: Boolean by lazy { this.role == Role.Admin }
}
