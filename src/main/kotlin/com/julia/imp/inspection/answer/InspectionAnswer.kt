package com.julia.imp.inspection.answer

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class InspectionAnswer(
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val inspectionId: String,
    val questionId: String,
    val answerOption: AnswerOption
)