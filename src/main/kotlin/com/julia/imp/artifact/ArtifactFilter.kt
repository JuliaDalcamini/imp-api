package com.julia.imp.artifact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
enum class ArtifactFilter {
    @SerialName("active") Active,
    @SerialName("assignedToMe") AssignedToMe,
    @SerialName("archived") Archived,
    @SerialName("all") All;

    companion object {
        fun fromString(string: String) = Json.Default.decodeFromString<ArtifactFilter>(string)
    }
}