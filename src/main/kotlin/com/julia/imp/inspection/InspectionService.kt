package com.julia.imp.inspection

import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.auth.user.UserRepository
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.inspection.answer.InspectionAnswer
import com.julia.imp.inspection.answer.InspectionAnswerRepository
import com.julia.imp.project.ProjectRepository
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isMember
import io.ktor.server.plugins.NotFoundException

class InspectionService(
    private val repository: InspectionRepository,
    private val artifactRepository: ArtifactRepository,
    private val answerRepository: InspectionAnswerRepository,
    private val projectRepository: ProjectRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val userRepository: UserRepository
) {

    suspend fun create(request: CreateInspectionRequest, artifactId: String, loggedUserId: String): String {

        if (!isInspector(loggedUserId, artifactId)) {
            throw UnauthorizedError("Only an assigned inspector can inspect")
        }

        val inspectionId = repository.insert(
            Inspection(
                artifactId = artifactId,
                inspectorId = loggedUserId,
                duration = request.duration,
                lastUpdate = request.lastUpdate
            )
        )

        for (pair in request.answers.entries) {
            answerRepository.insert(
                InspectionAnswer(
                    inspectionId = inspectionId,
                    questionId = pair.key,
                    answer = pair.value
                )
            )
        }

        return inspectionId
    }

    suspend fun getAll(artifactId: String, loggedUserId: String): List<InspectionResponse> {

        if (!isProjectMember(loggedUserId, artifactId)) {
            throw UnauthorizedError("Only project members can view an inspection")
        }

        val inspections = repository.findByArtifactId(artifactId)

        return inspections.map { inspection ->
            val inspector = userRepository.findById(inspection.inspectorId)
                ?: throw NotFoundException("Inspector not found")

            InspectionResponse.of(
                inspection = inspection,
                inspector = inspector
            )
        }
    }

    suspend fun update(inspectionId: String, request: UpdateInspectionRequest, loggedUserId: String) {
        val oldInspection = repository.findById(inspectionId)
            ?: throw NotFoundException("Inspection not found")

        if (!isInspector(loggedUserId, oldInspection.artifactId)) {
            throw UnauthorizedError("Only assigned inspector can update an inspection")
        }

        repository.replaceById(
            id = oldInspection.id.toString(),
            item = oldInspection.copy(
                duration = request.duration
            )
        )
    }

    private suspend fun isInspector(userId: String, artifactId: String): Boolean {
        val artifact = artifactRepository.findById(artifactId) ?: return false
        return artifact.inspectorIds.contains(userId)
    }

    private suspend fun isProjectMember(userId: String, artifactId: String): Boolean {
        val artifact = artifactRepository.findById(artifactId) ?: return false
        val project = projectRepository.findById(artifact.projectId) ?: return false
        return teamMemberRepository.isMember(userId, project.teamId)
    }
}
