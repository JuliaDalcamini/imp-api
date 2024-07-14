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
    val question: String,
    val artifactTypeId: String,
    val severity: String,
    val answer: Answer?,
    val quantity: Int,
    val observation: String,
    val defectTypeId: String
)
