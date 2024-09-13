package com.julia.imp.question

import kotlinx.serialization.Serializable

@Serializable
data class QuestionResponse(
    val id: String,
    val text: String
) {

    companion object {

        fun of(question: Question) = QuestionResponse(
            id = question.id.toString(),
            text = question.text
        )
    }
}
