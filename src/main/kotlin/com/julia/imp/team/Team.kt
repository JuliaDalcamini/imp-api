package com.julia.imp.team

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Team(
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val name: String,
    val defaultHourlyCost: Double
)