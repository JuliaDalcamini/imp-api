package com.julia.imp.report

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Report(
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val projectId: String,
    
)
