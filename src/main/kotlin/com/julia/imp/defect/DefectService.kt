package com.julia.imp.defect

import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.defecttype.DefectTypeRepository
import com.julia.imp.inspection.answer.InspectionAnswerRepository
import com.julia.imp.project.ProjectRepository
import com.julia.imp.question.QuestionRepository
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isMember

class DefectService(
    private val defectRepository: DefectRepository,
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

        val defects = defectRepository.findFiltered(
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

    private suspend fun isUserMember(loggedUserId: String, projectId: String): Boolean {
        val project = projectRepository.findById(projectId) ?: return false
        return teamMemberRepository.isMember(loggedUserId, project.teamId)
    }
}