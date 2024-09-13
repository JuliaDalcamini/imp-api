package com.julia.imp.report

import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.artifact.type.ArtifactType
import com.julia.imp.artifact.type.ArtifactTypeRepository
import com.julia.imp.artifact.type.ArtifactTypeResponse
import com.julia.imp.auth.user.UserRepository
import com.julia.imp.auth.user.UserResponse
import com.julia.imp.common.datetime.sumOfDuration
import com.julia.imp.common.math.average
import com.julia.imp.common.math.percentage
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.defecttype.DefectType
import com.julia.imp.defecttype.DefectTypeRepository
import com.julia.imp.defecttype.DefectTypeResponse
import com.julia.imp.inspection.Inspection
import com.julia.imp.inspection.InspectionRepository
import com.julia.imp.inspection.answer.AnswerOption
import com.julia.imp.inspection.answer.InspectionAnswerRepository
import com.julia.imp.project.ProjectRepository
import com.julia.imp.project.ProjectResponse
import com.julia.imp.project.dashboard.DashboardDefect
import com.julia.imp.question.QuestionRepository
import com.julia.imp.question.Severity
import com.julia.imp.team.TeamRepository
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isMember
import io.ktor.server.plugins.NotFoundException
import kotlin.time.Duration

class ReportService(
    private val userRepository: UserRepository,
    private val teamRepository: TeamRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val projectRepository: ProjectRepository,
    private val artifactRepository: ArtifactRepository,
    private val inspectionRepository: InspectionRepository,
    private val inspectionAnswerRepository: InspectionAnswerRepository,
    private val questionRepository: QuestionRepository,
    private val artifactTypeRepository: ArtifactTypeRepository,
    private val defectTypeRepository: DefectTypeRepository
) {

    suspend fun get(projectId: String, loggedUserId: String): ReportResponse {
        val project = getProjectResponse(projectId)

        if (!teamMemberRepository.isMember(loggedUserId, project.team.id)) {
            throw UnauthorizedError("Only team members can see its projects")
        }

        val artifacts = artifactRepository.findByProjectId(projectId)
        val artifactTypes = getArtifactTypes(artifacts)
        val inspections = artifacts.flatMap { inspectionRepository.findByArtifactId(it.id) }
        val defectTypes = getDefects(inspections).mapNotNull { defectTypeRepository.findById(it.defectTypeId) }
        val inspectors = getProjectInspectors(projectId)
        val projectCost = inspections.sumOf { it.cost }
        val projectEffort = inspections.sumOfDuration { it.duration }

        val inspectorsOverview = inspectors.map { inspector ->
            getInspectorOverview(artifacts, inspector, inspections)
        }

        val artifactTypeAndSeverityOfDefects = artifactTypes.map { artifactType ->
            getArtifactTypeAndSeverityOfDefect(
                artifactType = artifactType,
                projectCost = projectCost,
                artifacts = artifacts,
                inspections = inspections,
                weighted = false
            )
        }

        val weightedArtifactTypeAndSeverityOfDefects = artifactTypes.map { artifactType ->
            getArtifactTypeAndSeverityOfDefect(
                artifactType = artifactType,
                projectCost = projectCost,
                artifacts = artifacts,
                inspections = inspections,
                weighted = true
            )
        }

        val effortPerArtifactsType = artifactTypes.map { artifactType ->
            getEffortPerArtifactsType(
                artifactType = artifactType,
                artifacts = artifacts,
                inspections = inspections
            )
        }

        val costAndEffortPerDefectType = defectTypes.map { defectType ->
            getCostAndEffortPerDefectType(
                defectType = defectType,
                projectCost = projectCost,
                projectEffort = projectEffort,
                inspections = inspections
            )
        }

        return ReportResponse(
            project = project,
            overview = getOverview(projectCost, projectEffort, artifacts, inspections),
            inspectorsOverview = inspectorsOverview,
            defectsAndSeverityPerArtifactType = artifactTypeAndSeverityOfDefects,
            weightedDefectsAndSeverityPerArtifactType = weightedArtifactTypeAndSeverityOfDefects,
            effortPerArtifactsType = effortPerArtifactsType,
            costAndEffortPerDefectType = costAndEffortPerDefectType,
        )
    }

    private suspend fun getDefects(inspections: List<Inspection>): List<DashboardDefect> {
        val answers = inspections.flatMap { inspection ->
            inspectionAnswerRepository.findByInspectionId(inspection.id)
        }
        val negativeAnswers = answers.filter { it.answerOption == AnswerOption.No }

        return negativeAnswers.map { answer ->
            val question = questionRepository.findById(answer.questionId)
                ?: throw IllegalStateException("Question not found")

            DashboardDefect(
                defectTypeId = question.defectTypeId,
                artifactTypeId = question.artifactTypeId,
                severity = question.severity
            )
        }
    }

    private suspend fun getArtifactTypes(artifacts: List<Artifact>): List<ArtifactType> =
        artifacts
            .map { it.artifactTypeId }
            .distinct()
            .mapNotNull { artifactTypeRepository.findById(it) }

    private suspend fun getProjectInspectors(projectId: String): List<UserResponse> {
        val project = projectRepository.findById(projectId)
            ?: throw NotFoundException("Project not found")

        val inspectors = teamMemberRepository.findInspectorsByTeamId(project.teamId)

        return inspectors.mapNotNull { inspector ->
            val user = userRepository.findById(inspector.userId)
            user?.let { UserResponse.of(it) }
        }
    }

    private suspend fun getProjectResponse(projectId: String): ProjectResponse {
        val project = projectRepository.findById(projectId)
            ?: throw NotFoundException("Project not found")

        val creator = userRepository.findById(project.creatorId)
            ?: throw IllegalStateException("User not found")

        val team = teamRepository.findById(project.teamId)
            ?: throw IllegalStateException("Team not found")

        return ProjectResponse.of(project, creator, team)
    }

    private fun getOverview(
        totalCost: Double,
        totalEffort: Duration,
        artifacts: List<Artifact>,
        inspections: List<Inspection>
    ): ReportOverview {
        val averageCostPerArtifact = average(totalCost, artifacts.size)
        val averageCostPerInspection = average(totalCost, inspections.size)

        val averageEffortPerArtifact = average(totalEffort, artifacts.size)
        val averageEffortPerInspection = average(totalEffort, inspections.size)

        return ReportOverview(
            totalCost = totalCost,
            averageCostPerArtifact = averageCostPerArtifact,
            averageCostPerInspection = averageCostPerInspection,
            totalEffort = totalEffort,
            averageEffortPerArtifact = averageEffortPerArtifact,
            averageEffortPerInspection = averageEffortPerInspection,
            totalArtifacts = artifacts.size,
            artifactsInspected = artifacts.filter { it.inspected }.size
        )
    }

    private suspend fun getInspectorOverview(
        artifacts: List<Artifact>,
        inspector: UserResponse,
        projectsInspections: List<Inspection>
    ): InspectorOverview {
        val inspectorArtifacts = artifacts.filter { it.inspectorIds.contains(inspector.id) && !it.archived }
        val totalInspections = inspectorArtifacts.size

        val completedInspections = inspectorArtifacts.count { artifact ->
            val inspection = inspectionRepository.findByArtifactId(artifact.id)
                .filter { it.inspectorId == inspector.id }
                .maxByOrNull { it.createdAt }

            inspection?.artifactVersion == artifact.currentVersion
        }

        val totalEffort = projectsInspections
            .filter { it.inspectorId == inspector.id }
            .sumOfDuration { it.duration }

        val percentage = percentage(completedInspections, totalInspections)

        return InspectorOverview(
            inspector = inspector,
            totalInspections = totalInspections,
            completedInspections = completedInspections,
            percentage = percentage,
            totalEffort = totalEffort,
            averageEffort = average(totalEffort, completedInspections),
        )
    }

    private suspend fun getArtifactTypeAndSeverityOfDefect(
        artifactType: ArtifactType,
        projectCost: Double,
        artifacts: List<Artifact>,
        inspections: List<Inspection>,
        weighted: Boolean
    ): ArtifactTypeAndSeverityOfDefect {
        val projectDefects = getDefects(inspections)
        val defectsOfArtifactType = projectDefects.filter { it.artifactTypeId == artifactType.id.toString() }

        // Ajusta a severidade se for ponderado
        val (lowSeverity, mediumSeverity, highSeverity) = calculateSeverities(defectsOfArtifactType, weighted)

        val all = defectsOfArtifactType.size
        val distribution = percentage(defectsOfArtifactType.size, projectDefects.size)
        val totalCost = distribution * projectCost
        val artifactsOfType = artifacts.filter { it.artifactTypeId == artifactType.id.toString() }
        val averageCost = average(totalCost, artifactsOfType.size)

        val detectionEffort = artifactsOfType.sumOfDuration { artifact ->
            getEffortPerArtifact(inspections, artifact.id.toString())
        }

        // Calcula os custos de severidade
        val (lowSeverityCost, mediumSeverityCost, highSeverityCost) = calculateSeverityCosts(
            lowSeverity, mediumSeverity, highSeverity, totalCost, all
        )

        return ArtifactTypeAndSeverityOfDefect(
            artifactType = ArtifactTypeResponse.of(artifactType),
            lowSeverity = lowSeverity,
            mediumSeverity = mediumSeverity,
            highSeverity = highSeverity,
            all = all,
            totalCost = (defectsOfArtifactType.size / projectDefects.size) * totalCost,
            totalArtifacts = artifactsOfType.size,
            averageCost = averageCost,
            detectionEffort = detectionEffort,
            lowSeverityCost = lowSeverityCost,
            mediumSeverityCost = mediumSeverityCost,
            highSeverityCost = highSeverityCost,
        )
    }

    private fun calculateSeverities(
        defectsOfArtifactType: List<DashboardDefect>,
        weighted: Boolean
    ): Triple<Int, Int, Int> {
        val lowSeverity = defectsOfArtifactType.count { it.severity == Severity.Low }
        val mediumSeverity = defectsOfArtifactType.count { it.severity == Severity.Medium }
        val highSeverity = defectsOfArtifactType.count { it.severity == Severity.High }

        return if (weighted) {
            Triple(lowSeverity, mediumSeverity * 2, highSeverity * 3)
        } else {
            Triple(lowSeverity, mediumSeverity, highSeverity)
        }
    }

    private fun calculateSeverityCosts(
        lowSeverity: Int,
        mediumSeverity: Int,
        highSeverity: Int,
        totalCost: Double,
        all: Int
    ): Triple<Double, Double, Double> {
        return if (all > 0) {
            Triple(
                lowSeverity * totalCost / all,
                mediumSeverity * totalCost / all,
                highSeverity * totalCost / all
            )
        } else {
            Triple(0.0, 0.0, 0.0)
        }
    }

    private fun getEffortPerArtifact(inspections: List<Inspection>, artifactId: String): Duration =
        inspections.filter { it.artifactId == artifactId }.sumOfDuration { it.duration }

    private fun getCostPerArtifact(inspections: List<Inspection>, artifactId: String): Double =
        inspections.filter { it.artifactId == artifactId }.sumOf { it.cost }

    private fun getEffortPerArtifactsType(
        artifactType: ArtifactType,
        artifacts: List<Artifact>,
        inspections: List<Inspection>
    ): EffortPerArtifactsType {
        val artifactsOfType = artifacts.filter { it.artifactTypeId == artifactType.id.toString() }

        val totalEffort = artifactsOfType.sumOfDuration { artifact ->
            getEffortPerArtifact(inspections, artifact.id.toString())
        }

        return EffortPerArtifactsType(
            artifactType = ArtifactTypeResponse.of(artifactType),
            totalEffort = totalEffort,
            totalArtifacts = artifactsOfType.size,
            averageEffort = average(totalEffort, artifactsOfType.size),
        )
    }

    private suspend fun getCostAndEffortPerDefectType(
        defectType: DefectType,
        projectCost: Double,
        projectEffort: Duration,
        inspections: List<Inspection>
    ): CostAndEffortPerDefectType {
        val defects = getDefects(inspections)
        val quantity = defects.count { it.defectTypeId == defectType.id.toString() }

        val percentage = percentage(quantity, defects.size)
        val averageCost = percentage * projectCost
        val averageEffort = projectEffort * percentage

        return CostAndEffortPerDefectType(
            defectType = DefectTypeResponse.of(defectType),
            quantity = quantity,
            percentage = percentage,
            averageCost = averageCost,
            averageEffort = averageEffort
        )
    }
}