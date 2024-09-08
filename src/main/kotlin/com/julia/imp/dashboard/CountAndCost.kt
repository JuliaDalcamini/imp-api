package com.julia.imp.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class CountAndCost(
    val count: Int,
    val cost: Double
)
