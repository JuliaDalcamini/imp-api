package com.julia.imp.checklist

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

class DefectType(
    @BsonId
    val id: ObjectId,
    val name: String
)
