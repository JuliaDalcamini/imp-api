package com.julia.imp.project.dashboard

import com.julia.imp.defect.type.DefectTypeResponse
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
