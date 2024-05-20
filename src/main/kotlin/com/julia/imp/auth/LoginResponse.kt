package com.julia.imp.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)