package com.julia.imp.inspection

import com.julia.imp.question.Question
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Inspection(
    @Contextual
    @SerialName("_id")
    val id: ObjectId,
    val artifactId: String,
    val inspector: String,
    val start: Instant,
    val end: Instant,
    val questions: List<Question>
)