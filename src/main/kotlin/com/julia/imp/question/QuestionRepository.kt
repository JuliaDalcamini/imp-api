package com.julia.imp.question

import com.julia.imp.common.db.CrudRepository
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class QuestionRepository(database: MongoDatabase) : CrudRepository<Question>() {

    override val collection: MongoCollection<Question> = database.getCollection("questions")

    suspend fun findByArtifactTypeId(artifactTypeId: String): List<Question> =
        collection
            .find(Filters.eq("artifactTypeId", artifactTypeId))
            .toList()
}