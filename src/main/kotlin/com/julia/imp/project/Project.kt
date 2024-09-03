package com.julia.imp.project

import com.julia.imp.priority.Prioritizer
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Project(
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val name: String,
    val creationDateTime: Instant,
    val creatorId: String,
    val prioritizer: Prioritizer,
    val totalInspectors: Int,
    val teamId: String
)