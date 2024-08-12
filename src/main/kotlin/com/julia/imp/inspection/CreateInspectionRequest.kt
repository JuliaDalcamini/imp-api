package com.julia.imp.inspection

import com.julia.imp.question.Answer
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class CreateInspectionRequest(
    val duration: Duration,
    val answers: Map<String, Answer>
)