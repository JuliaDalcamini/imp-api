package com.julia.imp.artifact

import com.julia.imp.priority.Priority
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import java.time.LocalDateTime

@Serializable
data class Artifact(
    @Contextual
    @SerialName("_id")
    val id: ObjectId,
    val name: String,
    val artifactType: String,
    val creatorId: String,
    val inspectors: List<String>,
    @Contextual
    val creationDateTime: LocalDateTime,
    val priority: Priority
)