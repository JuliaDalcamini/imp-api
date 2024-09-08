package com.julia.imp.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class CostOverview(
    val total: Double,
    val averagePerArtifact: Double,
    val averagePerInspection: Double
)
