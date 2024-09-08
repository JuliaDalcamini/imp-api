package com.julia.imp.dashboard

import com.julia.imp.defecttype.DefectTypeResponse
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class DefectTypeSummary(
    val defectType: DefectTypeResponse,
    val percentage: Double,
    val count: Int,
    val averageCost: Double,
    val averageEffort: Duration
)
