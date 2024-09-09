package com.julia.imp.report

import com.julia.imp.project.ProjectResponse
import kotlinx.serialization.Serializable

@Serializable
data class ReportResponse(
    val project: ProjectResponse,
    val overview: ReportOverview,
    val inspectorsOverview: List<InspectorOverview>,
    val defectsAndSeverityPerArtifactType: List<ArtifactTypeAndSeverityOfDefect>,
    val weightedDefectsAndSeverityPerArtifactType: List<ArtifactTypeAndSeverityOfDefect>,
    val effortPerArtifactsType: List<EffortPerArtifactsType>,
    val costAndEffortPerDefectType: List<CostAndEffortPerDefectType>
)