package com.julia.imp.inspection.answer

import com.julia.imp.question.Answer
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class InspectionAnswer(
    @Contextual
    @SerialName("_id")
    val id: ObjectId,
    val inspectionId: String,
    val questionId: String,
    val answer: Answer
)