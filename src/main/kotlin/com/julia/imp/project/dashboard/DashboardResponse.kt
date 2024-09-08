package com.julia.imp.project.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    val overallProgress: OverallProgress,
    val effortOverview: EffortOverview,
    val costOverview: CostOverview,
    val inspectorProgress: List<InspectorProgress>,
    val artifactTypes: List<ArtifactTypeDefectSummary>,
    val defectsTypes: List<DefectTypeSummary>
)