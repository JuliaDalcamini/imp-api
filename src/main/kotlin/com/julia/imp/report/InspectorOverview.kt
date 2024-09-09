package com.julia.imp.report

import com.julia.imp.auth.user.UserResponse
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class InspectorOverview(
    val inspector: UserResponse,
    val totalInspections: Int,
    val completedInspections: Int,
    val percentage: Double,
    val totalEffort: Duration,
    val averageEffort: Duration
)
