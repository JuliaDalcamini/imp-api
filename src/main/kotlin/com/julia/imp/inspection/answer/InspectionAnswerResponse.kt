package com.julia.imp.inspection.answer

import com.julia.imp.question.Answer
import com.julia.imp.question.Question
import com.julia.imp.question.QuestionResponse
import kotlinx.serialization.Serializable

@Serializable
data class InspectionAnswerResponse(
    val id: String,
    val question: QuestionResponse,
    val answer: Answer
) {

    companion object {

        fun of(inspectionAnswer: InspectionAnswer, question: Question) = InspectionAnswerResponse(
            id = inspectionAnswer.id.toString(),
            question = QuestionResponse.of(question),
            answer = inspectionAnswer.answer
        )
    }
}