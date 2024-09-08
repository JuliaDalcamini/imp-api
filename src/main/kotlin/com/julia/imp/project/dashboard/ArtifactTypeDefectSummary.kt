package com.julia.imp.project.dashboard

import com.julia.imp.artifact.type.ArtifactTypeResponse
import kotlinx.serialization.Serializable

@Serializable
data class ArtifactTypeDefectSummary(
    val artifactType: ArtifactTypeResponse,
    val percentage: Double,
    val total: CountAndCost,
    val lowSeverity: CountAndCost,
    val mediumSeverity: CountAndCost,
    val highSeverity: CountAndCost
)
