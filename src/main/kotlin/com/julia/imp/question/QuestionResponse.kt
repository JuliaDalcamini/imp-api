package com.julia.imp.question

import kotlinx.serialization.Serializable

@Serializable
data class QuestionResponse(
    val id: String,
    val text: String,
    // TODO: Create and use Severity enum
    val severity: String,
    // TODO: Create and use DefectType class
    val defectTypeId: String
) {

    companion object {

        fun of(question: Question) = QuestionResponse(
            id = question.id.toString(),
            text = question.text,
            severity = question.severity,
            defectTypeId = question.defectTypeId
        )
    }
}
