package com.julia.imp.inspection.answer

import com.julia.imp.defecttype.DefectType
import com.julia.imp.question.Question
import com.julia.imp.question.QuestionResponse
import kotlinx.serialization.Serializable

@Serializable
data class InspectionAnswerResponse(
    val id: String,
    val question: QuestionResponse,
    val answer: AnswerOption
) {

    companion object {

        fun of(inspectionAnswer: InspectionAnswer, question: Question, defectType: DefectType) = InspectionAnswerResponse(
            id = inspectionAnswer.id.toString(),
            question = QuestionResponse.of(question, defectType),
            answer = inspectionAnswer.answer
        )
    }
}