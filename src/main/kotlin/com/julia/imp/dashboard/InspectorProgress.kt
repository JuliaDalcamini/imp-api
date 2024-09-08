package com.julia.imp.dashboard

import com.julia.imp.auth.user.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class InspectorProgress(
    val inspector: UserResponse,
    val percentage: Double,
    val count: Int,
    val total: Int
)
