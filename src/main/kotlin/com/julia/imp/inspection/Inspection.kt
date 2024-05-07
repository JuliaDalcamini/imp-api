package com.julia.imp.inspection

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Inspection (
    @BsonId
    val id: ObjectId
)