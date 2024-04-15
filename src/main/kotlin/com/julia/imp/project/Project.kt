package com.julia.imp.project

import com.julia.imp.artifact.Artifact
import com.julia.imp.auth.user.User
import com.julia.imp.checklist.Checklist
import com.julia.imp.prioritizationMethod.PrioritizationMethod
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class Project(
    @BsonId
    val id: ObjectId,
    private var nameProject: String,
    val creationDateTime: LocalDateTime,
    val creatorId: String,
    val prioritizationMethod: PrioritizationMethod,
    val checklist: Checklist,
    val artifactsList: ArrayList<Artifact>,
    val team: ArrayList<User>
)