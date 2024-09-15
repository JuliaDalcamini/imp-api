package com.julia.imp.defect

import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.defecttype.DefectTypeRepository
import com.julia.imp.inspection.answer.InspectionAnswerRepository
import com.julia.imp.project.ProjectRepository
import com.julia.imp.question.QuestionRepository
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isAdmin
import com.julia.imp.team.member.isMember
import io.ktor.server.plugins.NotFoundException

class DefectService(
    private val repository: DefectRepository,
    private val defectTypeRepository: DefectTypeRepository,
    private val projectRepository: ProjectRepository,
    private val artifactRepository: ArtifactRepository,
    private val inspectionAnswerRepository: InspectionAnswerRepository,
    private val questionRepository: QuestionRepository,
    private val teamMemberRepository: TeamMemberRepository
) {

    suspend fun get(
        artifactId: String,
        projectId: String,
        loggedUserId: String,
        filter: DefectFilter
    ): List<DefectResponse> {
        val project = projectRepository.findById(projectId)
            ?: throw IllegalStateException("Project not found")

        if (!isUserMember(loggedUserId, project.id.toString())) {
            throw UnauthorizedError("Only team members can see defects")
        }

        val artifact = artifactRepository.findById(artifactId)
            ?: throw IllegalStateException("Artifact not found")

        val defects = repository.findFiltered(
            artifactId = artifact.id.toString(),
            filter = filter
        )

        return defects.map { defect ->
            val type = defectTypeRepository.findById(defect.defectTypeId)
                ?: throw IllegalStateException("Defect type not found")

            val answer = inspectionAnswerRepository.findById(defect.answerId)
                ?: throw IllegalStateException("Answer not found")

            val question = questionRepository.findById(answer.questionId)
                ?: throw IllegalStateException("Question not found")

            DefectResponse.of(
                defect = defect,
                defectType = type,
                artifact = artifact,
                question = question
            )
        }
    }

    suspend fun update(
        request: UpdateDefectRequest,
        defectId: String,
        artifactId: String,
        projectId: String,
        loggedUserId: String
    ): DefectResponse {
        if (!isUserAdmin(loggedUserId, projectId)) {
            throw UnauthorizedError("Only admin can update artifacts")
        }

        val artifact = artifactRepository.findById(artifactId)
            ?: throw NotFoundException("Artifact not found")

        val oldDefect = repository.findById(defectId)
            ?: throw NotFoundException("Artifact not found")

        if (projectId != artifact.projectId || oldDefect.artifactId != artifactId || oldDefect.projectId != projectId) {
            throw NotFoundException("Defect not found")
        }

        val updatedDefect = oldDefect.copy(fixed = request.fixed)

        repository.replaceById(
            id = oldDefect.id.toString(),
            item = updatedDefect
        )

        val type = defectTypeRepository.findById(updatedDefect.defectTypeId)
            ?: throw IllegalStateException("Defect type not found")

        val answer = inspectionAnswerRepository.findById(updatedDefect.answerId)
            ?: throw IllegalStateException("Answer not found")

        val question = questionRepository.findById(answer.questionId)
            ?: throw IllegalStateException("Question not found")

        return DefectResponse.of(
            defect = updatedDefect,
            defectType = type,
            artifact = artifact,
            question = question
        )
    }

    private suspend fun isUserAdmin(loggedUserId: String, projectId: String): Boolean {
        val project = projectRepository.findById(projectId) ?: return false
        return teamMemberRepository.isAdmin(loggedUserId, project.teamId)
    }

    private suspend fun isUserMember(loggedUserId: String, projectId: String): Boolean {
        val project = projectRepository.findById(projectId) ?: return false
        return teamMemberRepository.isMember(loggedUserId, project.teamId)
    }
}