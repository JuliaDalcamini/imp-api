package com.julia.imp.question

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class QuestionRepository(private var database: MongoDatabase) {

    suspend fun insertOne(question: Question): String {
        val result = database.getCollection<Question>(COLLECTION).insertOne(question)
        return result.insertedId?.asObjectId()?.value?.toString() ?: throw IOException("Failed to create question")
    }

    suspend fun deleteById(id: ObjectId): Boolean {
        val result = database.getCollection<Question>(COLLECTION).deleteOne(Filters.eq("_id", id))
        return result.deletedCount > 0
    }

    suspend fun findAllQuestions(): List<Question> =
        database.getCollection<Question>(COLLECTION)
            .find()
            .toList()

    suspend fun findByArtifactTypeId(artifactTypeId: String): List<Question> =
        database.getCollection<Question>(COLLECTION)
            .find(Filters.eq("artifactTypeId", artifactTypeId))
            .toList()

    suspend fun findById(objectId: ObjectId): Question? =
        database.getCollection<Question>(COLLECTION)
            .find(Filters.eq("_id", objectId))
            .firstOrNull()

    suspend fun updateOne(id: ObjectId, question: Question): Boolean {
        val query = Filters.eq("_id", id)

        val updates = Updates.combine(
            //Updates.set(Question::name.name, question.name)
        )

        val result = database.getCollection<Question>(COLLECTION).updateOne(query, updates)

        return result.modifiedCount > 0
    }

    companion object {
        private const val COLLECTION = "questions"
    }
}