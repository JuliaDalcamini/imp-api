package com.julia.imp.auth.refresh

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokensRequest(
    val refreshToken: String
)
