package com.julia.imp.inspection

import com.julia.imp.inspection.answer.InspectionAnswer
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class UpdateInspectionRequest(
    val duration: Duration,
    val answers: List<InspectionAnswer>,
    val cost: Double
)