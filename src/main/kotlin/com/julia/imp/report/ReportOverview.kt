package com.julia.imp.report

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class ReportOverview (
    val totalCost: Double,
    val averageCostPerArtifact: Double,
    val averageCostPerInspection: Double,
    val totalEffort: Duration,
    val averageEffortPerArtifact: Duration,
    val averageEffortPerInspection: Duration,
    val totalArtifacts: Int,
    val artifactsInspected: Int
)
