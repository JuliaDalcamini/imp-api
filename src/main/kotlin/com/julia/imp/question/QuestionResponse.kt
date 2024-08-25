package com.julia.imp.question

import com.julia.imp.defecttype.DefectType
import com.julia.imp.defecttype.DefectTypeResponse
import kotlinx.serialization.Serializable

@Serializable
data class QuestionResponse(
    val id: String,
    val text: String,
    val severity: Severity,
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
