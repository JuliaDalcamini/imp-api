package com.julia.imp.dashboard

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class EffortOverview(
    val total: Duration,
    val averagePerArtifact: Duration,
    val averagePerInspection: Duration
)
