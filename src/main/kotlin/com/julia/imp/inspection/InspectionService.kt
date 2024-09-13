package com.julia.imp.inspection

import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.auth.user.UserRepository
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.defect.Defect
import com.julia.imp.defect.DefectRepository
import com.julia.imp.defecttype.DefectTypeRepository
import com.julia.imp.inspection.answer.AnswerOption
import com.julia.imp.inspection.answer.InspectionAnswer
import com.julia.imp.inspection.answer.InspectionAnswerRepository
import com.julia.imp.inspection.answer.InspectionAnswerResponse
import com.julia.imp.project.Project
import com.julia.imp.project.ProjectRepository
import com.julia.imp.question.QuestionRepository
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.canInspect
import com.julia.imp.team.member.isMember
import io.ktor.server.plugins.NotFoundException
import kotlinx.datetime.Clock
import kotlin.time.DurationUnit

class InspectionService(
    private val repository: InspectionRepository,
    private val artifactRepository: ArtifactRepository,
    private val answerRepository: InspectionAnswerRepository,
    private val questionRepository: QuestionRepository,
    private val defectTypeRepository: DefectTypeRepository,
    private val projectRepository: ProjectRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val userRepository: UserRepository,
    private val defectRepository: DefectRepository
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

        val project = projectRepository.findById(projectId)
            ?: throw NotFoundException("Project not found")

        val artifact = artifactRepository.findById(artifactId)
            ?: throw NotFoundException("Artifact not found")

        val member = teamMemberRepository.findByUserIdAndTeamId(loggedUserId, project.teamId)
            ?: throw IllegalStateException("Member not found")

        val inspection = repository.insertAndGet(
            Inspection(
                artifactId = artifactId,
                inspectorId = loggedUserId,
                duration = request.duration,
                createdAt = Clock.System.now(),
                cost = request.duration.toDouble(DurationUnit.HOURS) * member.hourlyCost,
                artifactVersion = artifact.currentVersion
            )
        )

        val answers = request.answers.map { answerRequest ->
            val question = questionRepository.findById(answerRequest.questionId)
                ?: throw IllegalStateException("Question not found")

            val defectType = defectTypeRepository.findById(question.defectTypeId)
                ?: throw IllegalStateException("Defect type not found")

            val answer = answerRepository.insertAndGet(
                InspectionAnswer(
                    inspectionId = inspection.id.toString(),
                    questionId = answerRequest.questionId,
                    answerOption = answerRequest.answer
                )
            )

            val defect = if (answerRequest.answer == AnswerOption.No) {
                defectRepository.insertAndGet(
                    Defect(
                        artifactId = artifactId,
                        defectTypeId = defectType.id.toString(),
                        answerId = answer.id.toString(),
                        severity = question.severity,
                        description = answerRequest.defectDescription,
                        fixed = false
                    )
                )
            } else null

            InspectionAnswerResponse.of(
                inspectionAnswer = answer,
                question = question,
                artifact = artifact,
                defect = defect,
                defectType = defectType
            )
        }

        updateArtifactInspectionState(artifact, project)

        val inspector = userRepository.findById(inspection.inspectorId)
            ?: throw IllegalStateException("Inspector not found")

        return InspectionResponse.of(
            inspection = inspection,
            inspector = inspector,
            answers = answers
        )
    }

    private suspend fun updateArtifactInspectionState(artifact: Artifact, project: Project) {
        val inspections = repository.findByArtifactId(artifact.id.toString())
        val inspectionCount = inspections.count { it.artifactVersion == artifact.currentVersion }
        val inspected = inspectionCount >= project.minInspectors

        if (inspected != artifact.inspected) {
            artifactRepository.replaceById(
                id = artifact.id,
                item = artifact.copy(inspected = inspected)
            )
        }
    }

    suspend fun getAll(artifactId: String, projectId: String, loggedUserId: String): List<InspectionResponse> {
        if (!canViewInspections(loggedUserId, artifactId, projectId)) {
            throw UnauthorizedError("Only team members can view inspections")
        }

        val artifact = artifactRepository.findById(artifactId)
            ?: throw NotFoundException("Artifact not found")

        val inspections = repository.findByArtifactId(artifactId)

        return inspections.map { inspection ->
            val inspector = userRepository.findById(inspection.inspectorId)
                ?: throw IllegalStateException("Inspector not found")

            val answers = answerRepository.findByInspectionId(inspection.id.toString()).map { answer ->
                val question = questionRepository.findById(answer.questionId)
                    ?: throw IllegalStateException("Question not found")

                val defect = defectRepository.findByAnswerId(answer.id)

                val defectType = defect?.let {
                    defectTypeRepository.findById(defect.defectTypeId)
                        ?: throw IllegalStateException("Defect type not found")
                }

                InspectionAnswerResponse.of(
                    inspectionAnswer = answer,
                    question = question,
                    artifact = artifact,
                    defect = defect,
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

    suspend fun get(
        artifactId: String,
        inspectionId: String,
        projectId: String,
        loggedUserId: String
    ): InspectionResponse {
        if (!canViewInspections(loggedUserId, artifactId, projectId)) {
            throw UnauthorizedError("Only team members can view inspections")
        }

        val artifact = artifactRepository.findById(artifactId)
            ?: throw NotFoundException("Artifact not found")

        val inspection = repository.findById(inspectionId)
            ?: throw NotFoundException("Inspection not found")

        val inspector = userRepository.findById(inspection.inspectorId)
            ?: throw IllegalStateException("Inspector not found")

        val answers = answerRepository.findByInspectionId(inspection.id.toString()).map { answer ->
            val question = questionRepository.findById(answer.questionId)
                ?: throw IllegalStateException("Question not found")

            val defect = defectRepository.findByAnswerId(answer.id)

            val defectType = defect?.let {
                defectTypeRepository.findById(defect.defectTypeId)
                    ?: throw IllegalStateException("Defect type not found")
            }

            InspectionAnswerResponse.of(
                inspectionAnswer = answer,
                question = question,
                artifact = artifact,
                defect = defect,
                defectType = defectType
            )
        }

        return InspectionResponse.of(
            inspection = inspection,
            inspector = inspector,
            answers = answers
        )
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
