package com.julia.imp.checklist

import com.julia.imp.question.Question
import org.bson.codecs.pojo.annotations.BsonId

data class ChecklistTemplate(
    @BsonId
    val id: String,
    val artifactTypes: ArrayList<String>,
    val questions: ArrayList<Question>
)
