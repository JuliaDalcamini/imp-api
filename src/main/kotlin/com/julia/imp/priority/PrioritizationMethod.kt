package com.julia.imp.priority

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PrioritizationMethod {

    @SerialName("moscow")
    Moscow,

    @SerialName("wiegers")
    Wiegers
}