package com.julia.imp.checklist

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class ChecklistTemplate(
    @Contextual
    @SerialName("_id")
    val id: ObjectId,
    val artifactTypes: List<String>,
    val questions: List<String>
) : Checklist()
