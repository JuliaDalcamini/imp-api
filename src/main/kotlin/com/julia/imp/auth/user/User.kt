package com.julia.imp.auth.user

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    @BsonId
    val id: ObjectId,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)
