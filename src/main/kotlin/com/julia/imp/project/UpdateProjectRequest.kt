package com.julia.imp.project

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProjectRequest(
    val name: String,
    val minInspectors: Int,
    val targetDate: LocalDate
)