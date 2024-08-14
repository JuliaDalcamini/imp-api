package com.julia.imp.question

import com.julia.imp.checklist.DefectType
import com.julia.imp.checklist.DefectTypeResponse
import kotlinx.serialization.Serializable

@Serializable
data class QuestionResponse(
    val id: String,
    val text: String,
    // TODO: Create and use Severity enum
    val severity: String,
    val defectType: DefectTypeResponse
) {

    companion object {

        fun of(question: Question, defectType: DefectType) = QuestionResponse(
            id = question.id.toString(),
            text = question.text,
            severity = question.severity,
            defectType = DefectTypeResponse.of(defectType)
        )
    }
}
