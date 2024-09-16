package com.julia.imp.project.dashboard

import com.julia.imp.artifact.type.ArtifactTypeResponse
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class ArtifactTypeSummary(
    val artifactType: ArtifactTypeResponse,
    val totalEffort: Duration,
    val totalCost: Double,
    val defects: ArtifactTypeDefects
)
