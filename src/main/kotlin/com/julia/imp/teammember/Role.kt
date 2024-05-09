package com.julia.imp.teammember

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Role {
    @SerialName("admin") Admin,
    @SerialName("inspector") Inspector,
    @SerialName("viewer") Viewer
}