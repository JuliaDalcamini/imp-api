package com.julia.imp.inspection.answer

import com.julia.imp.artifact.Artifact
import com.julia.imp.defect.Defect
import com.julia.imp.defect.DefectResponse
import com.julia.imp.defecttype.DefectType
import com.julia.imp.question.Question
import com.julia.imp.question.QuestionResponse
import kotlinx.serialization.Serializable

@Serializable
data class InspectionAnswerResponse(
    val id: String,
    val question: QuestionResponse,
    val answerOption: AnswerOption,
    val defect: DefectResponse?
) {

    companion object {

        fun of(
            inspectionAnswer: InspectionAnswer,
            question: Question,
            artifact: Artifact,
            defect: Defect?,
            defectType: DefectType?
        ) = InspectionAnswerResponse(
            id = inspectionAnswer.id.toString(),
            question = QuestionResponse.of(question),
            answerOption = inspectionAnswer.answerOption,
            defect = defect?.let {
                DefectResponse.of(
                    defect = defect,
                    defectType = defectType ?: throw IllegalArgumentException("Defect type is missing"),
                    artifact = artifact,
                    question = question
                )
            }
        )
    }
}