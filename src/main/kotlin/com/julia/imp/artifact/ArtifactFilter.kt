package com.julia.imp.artifact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
enum class ArtifactFilter {
    @SerialName("assignedToMe") AssignedToMe,
    @SerialName("prioritized") Prioritized,
    @SerialName("notPrioritized") NotPrioritized,
    @SerialName("archived") Archived,
    @SerialName("all") All;

    companion object {
        fun fromString(string: String) = Json.decodeFromString<ArtifactFilter>(string)
    }
}