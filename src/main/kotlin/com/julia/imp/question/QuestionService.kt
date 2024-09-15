package com.julia.imp.question

class QuestionService(
    private val repository: QuestionRepository
) {

    suspend fun getAll(artifactTypeId: String): List<QuestionResponse> {
        val questions = repository.findByArtifactTypeId(artifactTypeId)

        return questions.map { question ->
            QuestionResponse.of(question)
        }
    }
}
