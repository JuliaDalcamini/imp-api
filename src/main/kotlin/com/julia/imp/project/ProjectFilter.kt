package com.julia.imp.project

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
enum class ProjectFilter {
    @SerialName("active") Active,
    @SerialName("finished") Finished,
    @SerialName("all") All;

    companion object {
        fun fromString(string: String) = Json.decodeFromString<ProjectFilter>(string)
    }
}