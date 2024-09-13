package com.julia.imp.project.dashboard

import com.julia.imp.question.Severity

data class DashboardDefect(
    val defectTypeId: String,
    val artifactTypeId: String,
    val severity: Severity
)