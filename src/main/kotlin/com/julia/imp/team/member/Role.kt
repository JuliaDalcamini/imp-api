package com.julia.imp.team.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Role(val code: String) {
    @SerialName("admin") Admin("admin"),
    @SerialName("inspector") Inspector("inspector"),
    @SerialName("viewer") Viewer("viewer")
}