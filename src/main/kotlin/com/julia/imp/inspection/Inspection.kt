package com.julia.imp.inspection

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Inspection (
    @Contextual
    @SerialName("_id")
    val id: ObjectId
)