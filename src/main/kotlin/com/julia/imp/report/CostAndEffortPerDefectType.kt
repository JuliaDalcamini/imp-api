package com.julia.imp.report

import com.julia.imp.defecttype.DefectTypeResponse
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class CostAndEffortPerDefectType(
    val defectType: DefectTypeResponse,
    val quantity: Int,
    val percentage: Double,
    val averageCost: Double,
    val averageEffort: Duration
)
