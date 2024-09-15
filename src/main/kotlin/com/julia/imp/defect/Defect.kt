package com.julia.imp.defect

import com.julia.imp.question.Severity
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Defect(
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val artifactId: String,
    val projectId: String,
    val answerId: String,
    val defectTypeId: String,
    val severity: Severity,
    val description: String?,
    val fixed: Boolean
)