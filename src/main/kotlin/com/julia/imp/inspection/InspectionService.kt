package com.julia.imp.inspection

import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.auth.user.UserRepository
import com.julia.imp.checklist.DefectTypeRepository
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.inspection.answer.InspectionAnswer
import com.julia.imp.inspection.answer.InspectionAnswerRepository
import com.julia.imp.inspection.answer.InspectionAnswerResponse
import com.julia.imp.project.ProjectRepository
import com.julia.imp.question.QuestionRepository
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.canInspect
import com.julia.imp.team.member.isMember
import kotlinx.datetime.Clock

class InspectionService(
    private val repository: InspectionRepository,
    private val artifactRepository: ArtifactRepository,
    private val answerRepository: InspectionAnswerRepository,
    private val questionRepository: QuestionRepository,
    private val defectTypeRepository: DefectTypeRepository,
    private val projectRepository: ProjectRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val userRepository: UserRepository
) {

    suspend fun create(
        request: CreateInspectionRequest,
        artifactId: String,
        projectId: String,
        loggedUserId: String
    ): InspectionResponse {
        if (!canInspectArtifact(loggedUserId, artifactId, projectId)) {
            throw UnauthorizedError("Only assigned inspectors can inspect")
        }

        val inspection = repository.insertAndGet(
            Inspection(
                artifactId = artifactId,
                inspectorId = loggedUserId,
                duration = request.duration,
                createdAt = Clock.System.now()
            )
        )

        val answers = request.answers.entries.map { pair ->
            val answer = answerRepository.insertAndGet(
                InspectionAnswer(
                    inspectionId = inspection.id.toString(),
                    questionId = pair.key,
                    answer = pair.value
                )
            )

            val question = questionRepository.findById(pair.key)
                ?: throw IllegalStateException("Question not found")

            val defectType = defectTypeRepository.findById(question.defectTypeId)
                ?: throw IllegalStateException("Defect type not found")

            InspectionAnswerResponse.of(
                inspectionAnswer = answer,
                question = question,
                defectType = defectType
            )
        }

        val inspector = userRepository.findById(inspection.inspectorId)
            ?: throw IllegalStateException("Inspector not found")

        return InspectionResponse.of(
            inspection = inspection,
            inspector = inspector,
            answers = answers
        )
    }

    suspend fun getAll(artifactId: String, projectId: String, loggedUserId: String): List<InspectionResponse> {
        if (!canViewInspections(loggedUserId, artifactId, projectId)) {
            throw UnauthorizedError("Only team members can view inspections")
        }

        val inspections = repository.findByArtifactId(artifactId)

        return inspections.map { inspection ->
            val inspector = userRepository.findById(inspection.inspectorId)
                ?: throw IllegalStateException("Inspector not found")

            val answers = answerRepository.findByInspectionId(inspection.id.toString()).map {
                val question = questionRepository.findById(it.questionId)
                    ?: throw IllegalStateException("Question not found")

                val defectType = defectTypeRepository.findById(question.defectTypeId)
                    ?: throw IllegalStateException("Defect type not found")

                InspectionAnswerResponse.of(
                    inspectionAnswer = it,
                    question = question,
                    defectType = defectType
                )
            }

            InspectionResponse.of(
                inspection = inspection,
                inspector = inspector,
                answers = answers
            )
        }
    }

    private suspend fun canInspectArtifact(userId: String, artifactId: String, projectId: String): Boolean {
        val artifact = artifactRepository.findById(artifactId) ?: return false

        return if (projectId == artifact.projectId) {
            val project = projectRepository.findById(projectId) ?: return false
            val isTeamInspector = teamMemberRepository.canInspect(userId, project.teamId)

            isTeamInspector && artifact.inspectorIds.contains(userId)
        } else {
            false
        }
    }

    private suspend fun canViewInspections(userId: String, artifactId: String, projectId: String): Boolean {
        val artifact = artifactRepository.findById(artifactId) ?: return false

        return if (projectId == artifact.projectId) {
            val project = projectRepository.findById(artifact.projectId) ?: return false
            teamMemberRepository.isMember(userId, project.teamId)
        } else {
            false
        }
    }
}
