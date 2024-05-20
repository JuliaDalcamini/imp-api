package com.julia.imp.project

import kotlinx.serialization.Serializable

@Serializable
data class CreateProjectResponse(
    val projectId: String
)
