package com.julia.imp.inspection

import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import kotlin.time.Duration

@Serializable
data class Inspection(
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val artifactId: String,
    val inspectorId: String,
    val duration: Duration,
    val lastUpdate: Instant
)