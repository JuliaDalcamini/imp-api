package com.julia.imp.project

import com.julia.imp.priority.Prioritizer
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import org.bson.types.ObjectId

data class Project(
    @Contextual
    @SerialName("_id")
    val id: ObjectId,
    val name: String,
    val creationDateTime: Instant,
    val creatorId: String,
    val prioritizer: Prioritizer,
    // TODO: Implement and remove nullability
//    val checklist: Checklist?,
//    val artifactsList: List<Artifact>,
    val teamId: String
)