package com.julia.imp.project.dashboard

import com.julia.imp.auth.user.UserResponse
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class InspectorSummary(
    val inspector: UserResponse,
    val totalEffort: Duration,
    val totalCost: Double,
    val progress: Progress
)
