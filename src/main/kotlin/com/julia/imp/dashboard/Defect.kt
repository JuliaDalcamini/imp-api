package com.julia.imp.dashboard

import com.julia.imp.question.Severity
import kotlinx.serialization.Serializable

@Serializable
data class Defect(
    val defectTypeId: String,
    val artifactTypeId: String,
    val severity: Severity
)