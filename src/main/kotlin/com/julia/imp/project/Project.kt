package com.julia.imp.project

import com.julia.imp.auth.user.User
import com.julia.imp.priority.Prioritizer
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class Project(
    @BsonId
    val id: ObjectId,
    val name: String,
    val creationDateTime: LocalDateTime,
    val creatorId: String,
    val prioritizer: Prioritizer,
    // TODO: Implement and remove nullability
//    val checklist: Checklist?,
//    val artifactsList: List<Artifact>,
    val team: List<User>
)