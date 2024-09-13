package com.julia.imp.defect

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
enum class DefectFilter {
    @SerialName("notfixed") NotFixed,
    @SerialName("Fixed") Fixed,
    @SerialName("all") All;

    companion object {
        fun fromString(string: String) = Json.decodeFromString<DefectFilter>(string)
    }
}