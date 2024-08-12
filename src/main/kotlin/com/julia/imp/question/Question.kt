package com.julia.imp.question

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Question(
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val text: String,
    val artifactTypeId: String,
    val severity: String,
    val defectTypeId: String
)
