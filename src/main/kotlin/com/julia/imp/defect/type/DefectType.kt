package com.julia.imp.defect.type

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class DefectType(
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val name: String
)
