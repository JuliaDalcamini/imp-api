package com.julia.imp.artifact

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class ArtifactType (
    @BsonId
    val id: ObjectId,
    val name: String
)
