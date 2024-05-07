package com.julia.imp.priority

import kotlinx.serialization.SerialName

enum class MoscowPriorityLevel {
    @SerialName("wontHave") WontHave,
    @SerialName("couldHave") CouldHave,
    @SerialName("shouldHave") ShouldHave,
    @SerialName("mustHave") MustHave
}