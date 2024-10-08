package com.julia.imp.artifact

import com.julia.imp.priority.Priority
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Artifact(
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val name: String,
    val externalLink: String,
    val artifactTypeId: String,
    val projectId: String,
    val creatorId: String,
    val inspectorIds: List<String>,
    val creationDateTime: Instant,
    val priority: Priority?,
    val archived: Boolean,
    val lastModification: Instant,
    val currentVersion: String,
    val inspected: Boolean
)