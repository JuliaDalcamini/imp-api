package com.julia.imp.artifact

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class Artifact(
    @BsonId
    val id: ObjectId,
    val name: String,
    val artifactType: ObjectId,
    val creatorId: ObjectId,
    val status: Boolean,
    val creationDateTime: LocalDateTime,
    val conclusionDateTime: LocalDateTime,
    val priority: String
)