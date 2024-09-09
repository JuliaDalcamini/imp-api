package com.julia.imp.report

import com.julia.imp.artifact.type.ArtifactTypeResponse
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class EffortPerArtifactsType(
    val artifactType: ArtifactTypeResponse,
    val totalEffort: Duration,
    val totalArtifacts: Int,
    val averageEffort: Duration
)
