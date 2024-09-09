package com.julia.imp.report

import com.julia.imp.artifact.type.ArtifactTypeResponse
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class ArtifactTypeAndSeverityOfDefect(
    val artifactType: ArtifactTypeResponse,
    val lowSeverity: Int,
    val mediumSeverity: Int,
    val highSeverity: Int,
    val all: Int,
    val totalCost: Double,
    val totalArtifacts: Int,
    val averageCost: Double,
    val detectionEffort: Duration,
    val lowSeverityCost: Double,
    val mediumSeverityCost: Double,
    val highSeverityCost: Double
)
