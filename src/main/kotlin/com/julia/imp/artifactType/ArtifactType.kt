package com.julia.imp.artifactType

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class ArtifactType(
    @Contextual
    @SerialName("_id")
    val id: ObjectId,
    val name: String
)
