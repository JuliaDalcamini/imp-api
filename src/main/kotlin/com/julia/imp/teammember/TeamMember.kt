package com.julia.imp.teammember

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class TeamMember(
    @BsonId
    val id: ObjectId,
    val userId: String,
    val teamId: String,
    val role: Role
) {

    val isAdmin: Boolean by lazy { this.role == Role.Admin }
}
