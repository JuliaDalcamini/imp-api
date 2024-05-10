package com.julia.imp.checklist

import com.julia.imp.artifactType.ArtifactType
import com.julia.imp.question.Question
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class ChecklistTemplate(
    @Contextual
    @SerialName("_id")
    val id: ObjectId,
    val artifactTypes: List<ArtifactType>,
    val questions: List<Question>
) : Checklist()
