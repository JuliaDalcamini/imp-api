package com.julia.imp.question

import org.bson.codecs.pojo.annotations.BsonId

data class Question(
    @BsonId
    val id: String,
    val question: String,
    val artifactTypeId: String,
    val severity: String,
    val answer: Answer,
    val quantity: Int,
    val observation: String,
    val defectTypeId: String
)
