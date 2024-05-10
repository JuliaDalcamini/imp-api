package com.julia.imp.checklist

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class DefectType(
    @Contextual
    @SerialName("_id")
    val id: ObjectId,
    val name: String
)
