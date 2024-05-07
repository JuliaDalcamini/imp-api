package com.julia.imp.artifact

import com.julia.imp.priority.Priority
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class Artifact(
    @BsonId
    val id: ObjectId,
    val name: String,
    val artifactType: String,
    val creatorId: String,
    val status: Boolean,
    val creationDateTime: LocalDateTime,
    val conclusionDateTime: LocalDateTime?,
    val priority: Priority
)