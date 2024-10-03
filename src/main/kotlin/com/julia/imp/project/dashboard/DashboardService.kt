package com.julia.imp.project.dashboard

import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.artifact.type.ArtifactTypeRepository
import com.julia.imp.artifact.type.ArtifactTypeResponse
import com.julia.imp.auth.user.UserRepository
import com.julia.imp.auth.user.UserResponse
import com.julia.imp.common.datetime.atEndOfDay
import com.julia.imp.common.datetime.atStartOfDay
import com.julia.imp.common.datetime.sumOfDuration
import com.julia.imp.common.math.average
import com.julia.imp.common.math.percentage
import com.julia.imp.common.math.standardDeviation
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.defect.Defect
import com.julia.imp.defect.DefectRepository
import com.julia.imp.defect.type.DefectTypeRepository
import com.julia.imp.defect.type.DefectTypeResponse
import com.julia.imp.inspection.Inspection
import com.julia.imp.inspection.InspectionRepository
import com.julia.imp.project.Project
import com.julia.imp.project.ProjectRepository
import com.julia.imp.question.Severity
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isMember
import io.ktor.server.plugins.NotFoundException
import kotlin.time.Duration
import kotlinx.datetime.Clock

class DashboardService(
    private val userRepository: UserRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val projectRepository: ProjectRepository,
    private val artifactRepository: ArtifactRepository,
    private val inspectionRepository: InspectionRepository,
    private val defectRepository: DefectRepository,
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
        val defects = defectRepository.findByProjectId(projectId)
        val inspections = artifacts.flatMap { inspectionRepository.findByArtifactId(it.id) }
        val artifactsWithInspections = getArtifactsWithInspections(artifacts, inspections)
        val inspectorSummaries = calculateInspectorSummaries(artifacts, inspections)
        val inspectionProgress = calculateInspectionProgress(artifacts)
        val defectsProgress = calculateDefectsProgress(defects)
        val effortOverview = calculateEffortOverview(artifactsWithInspections, inspections)
        val costOverview = calculateCostOverview(artifactsWithInspections, inspections)
        val artifactTypeSummaries = calculateArtifactTypeSummaries(defects, artifacts, inspections)
        val defectsByDefectType = calculateDefectsByDefectType(defects, costOverview.total, effortOverview.total)
        val performanceScore = calculatePerformanceScore(project, inspectionProgress)

        return DashboardResponse(
            inspectionProgress = inspectionProgress,
            defectsProgress = defectsProgress,
            effortOverview = effortOverview,
            costOverview = costOverview,
            inspectors = inspectorSummaries,
            artifactTypes = artifactTypeSummaries,
            defectTypes = defectsByDefectType,
            performanceScore = performanceScore
        )
    }

    private fun calculateInspectionProgress(artifacts: List<Artifact>): Progress {
        val inspectedArtifacts = artifacts.filter { it.inspected && !it.archived }
        val total = artifacts.filter { !it.archived }.size
        val percentage = percentage(inspectedArtifacts.size, artifacts.size)

        return Progress(
            percentage = percentage,
            count = inspectedArtifacts.size,
            total = total
        )
    }

    private fun calculatePerformanceScore(project: Project, inspectionProgress: Progress): PerformanceScore {
        val expectedDuration = project.targetDate.atEndOfDay() - project.startDate.atStartOfDay()
        val elapsedDuration = (project.finishedAt ?: Clock.System.now()) - project.startDate.atStartOfDay()
        val elapsedDurationPercentage = elapsedDuration / expectedDuration

        return if (elapsedDurationPercentage > 0) {
            val idi = inspectionProgress.percentage / elapsedDurationPercentage

            when {
                idi >= .9 -> PerformanceScore.A
                idi >= .8 -> PerformanceScore.B
                idi >= .7 -> PerformanceScore.C
                else -> PerformanceScore.D
            }
        } else PerformanceScore.A
    }

    private fun calculateDefectsProgress(defects: List<Defect>): Progress {
        val fixedDefects = defects.filter { it.fixed }
        val total = defects.size
        val percentage = percentage(fixedDefects.size, defects.size)

        return Progress(
            percentage = percentage,
            count = fixedDefects.size,
            total = total
        )
    }

    private fun getArtifactsWithInspections(artifacts: List<Artifact>, inspections: List<Inspection>): List<Artifact> {
        val inspectedArtifactIds = inspections.groupBy { it.artifactId }.keys
        return artifacts.filter { it.id.toString() in inspectedArtifactIds }
    }

    private fun calculateEffortOverview(artifacts: List<Artifact>, inspections: List<Inspection>): EffortOverview {
        val durations = inspections.map { it.duration }
        val totalDuration = durations.sumOfDuration()
        val averagePerArtifact = average(totalDuration, artifacts.size)
        val averagePerInspection = average(totalDuration, inspections.size)
        val standardDeviationPerInspection = durations.standardDeviation()

        return EffortOverview(
            total = totalDuration,
            averagePerArtifact = averagePerArtifact,
            averagePerInspection = averagePerInspection,
            standardDeviationPerInspection = standardDeviationPerInspection
        )
    }

    private fun calculateCostOverview(artifacts: List<Artifact>, inspections: List<Inspection>): CostOverview {
        val costs = inspections.map { it.cost }
        val totalCost = costs.sum()
        val averagePerArtifact = average(totalCost, artifacts.size)
        val averagePerInspection = average(totalCost, inspections.size)
        val standardDeviationPerInspection = costs.standardDeviation()

        return CostOverview(
            total = totalCost,
            averagePerArtifact = averagePerArtifact,
            averagePerInspection = averagePerInspection,
            standardDeviationPerInspection = standardDeviationPerInspection
        )
    }

    private suspend fun calculateInspectorSummaries(
        projectArtifacts: List<Artifact>,
        projectInspections: List<Inspection>
    ): List<InspectorSummary> {
        val inspectionsByInspector = projectInspections.groupBy { it.inspectorId }

        return inspectionsByInspector.map { pair ->
            val (inspectorId, inspections) = pair
            val validArtifacts = projectArtifacts.filter { it.inspectorIds.contains(inspectorId) && !it.archived }
            val validArtifactIds = validArtifacts.map { it.id.toString() }

            val validInspections = inspections.filter {
                it.inspectorId == inspectorId && it.artifactId in validArtifactIds
            }

            val progress = if (validArtifacts.isNotEmpty()) {
                val inspected = validArtifacts.count { artifact ->
                    val inspection = validInspections
                        .filter { it.artifactId == artifact.id.toString() }
                        .maxByOrNull { it.createdAt }

                    inspection?.artifactVersion == artifact.currentVersion
                }

                Progress(
                    percentage = inspected.toDouble() / validArtifacts.size,
                    count = inspected,
                    total = validArtifacts.size
                )
            } else {
                Progress(
                    percentage = 0.0,
                    count = 0,
                    total = 0
                )
            }

            val inspector = userRepository.findById(inspectorId)
                ?: throw IllegalStateException("Inspector not found")

            val totalEffort = inspections.sumOfDuration { it.duration }
            val totalCost = inspections.sumOf { it.cost }

            InspectorSummary(
                inspector = UserResponse.of(inspector),
                totalEffort = totalEffort,
                totalCost = totalCost,
                progress = progress
            )
        }
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

            val percentage = percentage(defects.size, projectDefects.size)

            DefectTypeSummary(
                defectType = DefectTypeResponse.of(defectType),
                percentage = percentage,
                count = defects.size,
                averageCost = projectCost * percentage,
                averageEffort = projectEffort * percentage
            )
        }
    }

    private suspend fun calculateArtifactTypeSummaries(
        projectDefects: List<Defect>,
        projectArtifacts: List<Artifact>,
        projectInspections: List<Inspection>
    ): List<ArtifactTypeSummary> {
        val defectsByArtifactType = projectDefects.groupBy { defect ->
            val artifact = projectArtifacts.find { it.id.toString() == defect.artifactId }
                ?: throw IllegalStateException("Artifact not found")

            artifact.artifactTypeId
        }

        val inspectionsByArtifactType = projectInspections.groupBy { inspection ->
            val artifact = projectArtifacts.find { it.id.toString() == inspection.artifactId }
                ?: throw IllegalStateException("Artifact not found")

            artifact.artifactTypeId
        }

        return inspectionsByArtifactType.map { pair ->
            val (artifactTypeId, inspections) = pair
            val defects = defectsByArtifactType[artifactTypeId].orEmpty()

            val artifactType = artifactTypeRepository.findById(artifactTypeId)
                ?: throw IllegalStateException("Artifact type not found")

            val percentage = percentage(defects.size, projectDefects.size)
            val totalCost = inspections.sumOf { it.cost }
            val totalEffort = inspections.sumOfDuration { it.duration }

            ArtifactTypeSummary(
                artifactType = ArtifactTypeResponse.of(artifactType),
                totalEffort = totalEffort,
                totalCost = totalCost,
                defects = ArtifactTypeDefects(
                    percentage = percentage,
                    total = CountAndCost(count = defects.size, cost = totalCost),
                    lowSeverity = calculateCountAndCostForSeverity(Severity.Low, defects, totalCost),
                    mediumSeverity = calculateCountAndCostForSeverity(Severity.Medium, defects, totalCost),
                    highSeverity = calculateCountAndCostForSeverity(Severity.High, defects, totalCost)
                )
            )
        }
    }

    private fun calculateCountAndCostForSeverity(
        severity: Severity,
        defects: List<Defect>,
        cost: Double
    ): CountAndCost {
        val defectsOfSeverity = defects.filter { defect -> defect.severity == severity }
        val severityPercentage = percentage(defectsOfSeverity.size, defects.size)

        return CountAndCost(
            count = defectsOfSeverity.size,
            cost = severityPercentage * cost
        )
    }
}
