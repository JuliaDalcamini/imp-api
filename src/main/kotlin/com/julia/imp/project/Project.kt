package com.julia.imp.project

import com.julia.imp.priority.Prioritizer
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class Project(
    @Contextual
    @SerialName("_id")
    val id: ObjectId,
    val name: String,
    val creationDateTime: LocalDateTime,
    val creatorId: String,
    val prioritizer: Prioritizer,
    // TODO: Implement and remove nullability
//    val checklist: Checklist?,
//    val artifactsList: List<Artifact>,
    val teamId: String
)