package com.julia.imp.team

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Team(
    @BsonId
    val id: ObjectId,
    val name: String
)