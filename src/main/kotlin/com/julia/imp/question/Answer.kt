package com.julia.imp.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Answer {
    @SerialName("na") NotApplicable,
    @SerialName("yes") Yes,
    @SerialName("no") No
}
