package com.julia.imp.inspection

import com.julia.imp.inspection.answer.InspectionAnswerRequest
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class CreateInspectionRequest(
    val duration: Duration,
    val answers: List<InspectionAnswerRequest>
)