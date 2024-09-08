package com.julia.imp.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class OverallProgress(
    val percentage: Double,
    val count: Int,
    val total: Int
)
