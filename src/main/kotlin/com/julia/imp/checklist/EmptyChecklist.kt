package com.julia.imp.checklist

import com.julia.imp.question.Question
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class EmptyChecklist (
    @BsonId
    val id: ObjectId,
    val creatorId: String,
    val artifactTypes: ArrayList<String>,
    val questions: ArrayList<Question>
)
