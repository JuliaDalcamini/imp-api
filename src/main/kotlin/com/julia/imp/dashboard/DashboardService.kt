package com.julia.imp.dashboard

import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.artifact.type.ArtifactTypeRepository
import com.julia.imp.artifact.type.ArtifactTypeResponse
import com.julia.imp.auth.user.UserRepository
import com.julia.imp.auth.user.UserResponse
import com.julia.imp.common.datetime.sumOfDuration
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.defecttype.DefectTypeRepository
import com.julia.imp.defecttype.DefectTypeResponse
import com.julia.imp.inspection.Inspection
import com.julia.imp.inspection.InspectionRepository
import com.julia.imp.inspection.answer.AnswerOption
import com.julia.imp.inspection.answer.InspectionAnswerRepository
import com.julia.imp.project.ProjectRepository
import com.julia.imp.question.QuestionRepository
import com.julia.imp.question.Severity
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isMember
import io.ktor.server.plugins.*
import kotlin.time.Duration

class DashboardService(
    private val userRepository: UserRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val projectRepository: ProjectRepository,
    private val artifactRepository: ArtifactRepository,
    private val inspectionRepository: InspectionRepository,
    private val inspectionAnswerRepository: InspectionAnswerRepository,
    private val questionRepository: QuestionRepository,
    private val artifactTypeRepository: ArtifactTypeRepository,
    private val defectTypeRepository: DefectTypeRepository
) {

    suspend fun get(projectId: String, loggedUserId: String): DashboardResponse {
        val project = projectRepository.findById(projectId)
            ?: throw NotFoundException("Project not found")

        if (!teamMemberRepository.isMember(loggedUserId, project.teamId)) {
            throw UnauthorizedError("Only team members can see its projects")
        }

        val artifacts = artifactRepository.findByProjectId(projectId)
        val inspections = artifacts.flatMap { inspectionRepository.findByArtifactId(it.id) }
        val defects = getDefects(inspections)
        val inspectors = getProjectInspectors(projectId)
        val artifactsWithInspections = getArtifactsWithInspections(artifacts, inspections)

        val inspectorsProgress = inspectors.map { inspector ->
            calculateInspectorProgress(artifacts, inspector)
        }

        val effortOverview = calculateEffortOverview(artifactsWithInspections, inspections)
        val costOverview = calculateCostOverview(artifactsWithInspections, inspections)
        val overallProgress = calculateOverallProgress(artifacts)
        val defectsByArtifactType = calculateDefectsByArtifactType(defects, costOverview.total)
        val defectsByDefectType = calculateDefectsByDefectType(defects, costOverview.total, effortOverview.total)

        return DashboardResponse(
            overallProgress = overallProgress,
            effortOverview = effortOverview,
            costOverview = costOverview,
            inspectorProgress = inspectorsProgress,
            artifactTypes = defectsByArtifactType,
            defectsTypes = defectsByDefectType
        )
    }

    private suspend fun getProjectInspectors(projectId: String): List<UserResponse> {
        val project = projectRepository.findById(projectId)
            ?: throw NotFoundException("Project not found")

        val inspectors = teamMemberRepository.findInspectorsByTeamId(project.teamId)

        return inspectors.mapNotNull { inspector ->
            val user = userRepository.findById(inspector.userId)
            user?.let { UserResponse.of(it) }
        }
    }

    private fun getArtifactsWithInspections(artifacts: List<Artifact>, inspections: List<Inspection>): List<Artifact> {
        val inspectedArtifactIds = inspections.groupBy { it.artifactId }.keys
        return artifacts.filter { it.id.toString() in inspectedArtifactIds }
    }

    private fun calculateEffortOverview(artifacts: List<Artifact>, inspections: List<Inspection>): EffortOverview {
        val totalDuration = inspections.sumOfDuration { it.duration }
        val averagePerArtifact = if (artifacts.isNotEmpty()) totalDuration / artifacts.size else Duration.ZERO
        val averagePerInspection = if (inspections.isNotEmpty()) totalDuration / inspections.size else Duration.ZERO

        return EffortOverview(
            total = totalDuration,
            averagePerArtifact = averagePerArtifact,
            averagePerInspection = averagePerInspection
        )
    }

    private fun calculateCostOverview(artifacts: List<Artifact>, inspections: List<Inspection>): CostOverview {
        val totalCost = inspections.sumOf { it.cost }
        val averagePerArtifact = if (artifacts.isNotEmpty()) totalCost / artifacts.size else 0.0
        val averagePerInspection = if (inspections.isNotEmpty()) totalCost / inspections.size else 0.0

        return CostOverview(
            total = totalCost,
            averagePerArtifact = averagePerArtifact,
            averagePerInspection = averagePerInspection
        )
    }

    private suspend fun calculateInspectorProgress(
        artifacts: List<Artifact>,
        inspector: UserResponse
    ): InspectorProgress {
        val inspectorArtifacts = artifacts.filter { it.inspectorIds.contains(inspector.id) }
        val total = inspectorArtifacts.size

        val inspected = inspectorArtifacts.count { artifact ->
            val inspection = inspectionRepository.findByArtifactId(artifact.id)
                .filter { it.inspectorId == inspector.id }
                .maxByOrNull { it.createdAt }

            inspection?.artifactVersion == artifact.currentVersion
        }

        val percentage = if (total > 0) inspected.toDouble() / total else 0.0

        return InspectorProgress(
            inspector = inspector,
            percentage = percentage,
            count = inspected,
            total = total
        )
    }

    private fun calculateOverallProgress(artifacts: List<Artifact>): OverallProgress {
        val inspectedArtifacts = artifacts.filter { it.inspected }
        val total = artifacts.size
        val percentage = if (total > 0) inspectedArtifacts.size.toDouble() / artifacts.size else 0.0

        return OverallProgress(
            percentage = percentage,
            count = inspectedArtifacts.size,
            total = total
        )
    }

    private suspend fun calculateDefectsByDefectType(
        projectDefects: List<Defect>,
        projectCost: Double,
        projectEffort: Duration
    ): List<DefectTypeSummary> {
        val defectsByType = projectDefects.groupBy { it.defectTypeId }

        return defectsByType.map { pair ->
            val (defectTypeId, defects) = pair

            val defectType = defectTypeRepository.findById(defectTypeId)
                ?: throw IllegalStateException("Defect type not found")

            val percentage = if (projectDefects.isNotEmpty()) defects.size.toDouble() / projectDefects.size else 0.0

            DefectTypeSummary(
                defectType = DefectTypeResponse.of(defectType),
                percentage = percentage,
                count = defects.size,
                averageCost = projectCost * percentage,
                averageEffort = projectEffort * percentage
            )
        }
    }

    private suspend fun calculateDefectsByArtifactType(
        projectDefects: List<Defect>,
        projectCost: Double
    ): List<ArtifactTypeDefectSummary> {
        val defectsByArtifactType = projectDefects.groupBy { it.artifactTypeId }

        return defectsByArtifactType.map { pair ->
            val (artifactTypeId, defects) = pair

            val artifactType = artifactTypeRepository.findById(artifactTypeId)
                ?: throw IllegalStateException("Artifact type not found")

            val percentage = if (projectDefects.isNotEmpty()) defects.size.toDouble() / projectDefects.size else 0.0

            val totals = CountAndCost(
                count = defects.size,
                cost = projectCost * percentage
            )

            ArtifactTypeDefectSummary(
                artifactType = ArtifactTypeResponse.of(artifactType),
                percentage = percentage,
                total = totals,
                lowSeverity = calculateCountAndCostForSeverity(Severity.Low, defects, totals.cost),
                mediumSeverity = calculateCountAndCostForSeverity(Severity.Medium, defects, totals.cost),
                highSeverity = calculateCountAndCostForSeverity(Severity.High, defects, totals.cost)
            )
        }
    }

    private fun calculateCountAndCostForSeverity(
        severity: Severity,
        defects: List<Defect>,
        cost: Double
    ): CountAndCost {
        val defectsOfSeverity = defects.filter { defect -> defect.severity == severity }
        val severityPercentage = if (defects.isNotEmpty()) defectsOfSeverity.size.toDouble() / defects.size else 0.0

        return CountAndCost(
            count = defectsOfSeverity.size,
            cost = severityPercentage * cost
        )
    }

    private suspend fun getDefects(inspections: List<Inspection>): List<Defect> {
        val answers = inspections.flatMap { inspection -> inspectionAnswerRepository.findByInspectionId(inspection.id) }
        val negativeAnswers = answers.filter { it.answer == AnswerOption.No }

        return negativeAnswers.map { answer ->
            val question = questionRepository.findById(answer.questionId)
                ?: throw IllegalStateException("Question not found")

            Defect(
                defectTypeId = question.defectTypeId,
                artifactTypeId = question.artifactTypeId,
                severity = question.severity
            )
        }
    }
}
