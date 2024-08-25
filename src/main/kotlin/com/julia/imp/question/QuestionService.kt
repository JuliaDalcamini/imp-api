package com.julia.imp.question

import com.julia.imp.defecttype.DefectTypeRepository

class QuestionService(
    private val repository: QuestionRepository,
    private val defectTypeRepository: DefectTypeRepository
) {

    suspend fun getAll(artifactTypeId: String): List<QuestionResponse> {
        val questions = repository.findByArtifactTypeId(artifactTypeId)

        return questions.map { question ->
            val defectType = defectTypeRepository.findById(question.defectTypeId)
                ?: throw IllegalStateException("Defect type not found")

            QuestionResponse.of(
                question = question,
                defectType = defectType
            )
        }
    }
}
