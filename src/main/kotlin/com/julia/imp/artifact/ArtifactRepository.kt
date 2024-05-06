package com.julia.imp.artifact

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class ArtifactRepository(private var database: MongoDatabase) {

    suspend fun insertOne(artifact: Artifact): String? {
        val result = database.getCollection<Artifact>(COLLECTION).insertOne(artifact)
        return result.insertedId?.asObjectId()?.value?.toString()
    }

    suspend fun deleteById(id: ObjectId): Boolean {
        val result = database.getCollection<Artifact>(COLLECTION).deleteOne(Filters.eq("_id", id))
        return result.deletedCount > 0
    }

    suspend fun findById(objectId: ObjectId): Artifact? =
        database.getCollection<Artifact>(COLLECTION)
            .find(Filters.eq("_id", objectId))
            .firstOrNull()

    suspend fun updateOne(id: ObjectId, artifact: Artifact): Boolean {
        val query = Filters.eq("_id", id)

        val updates = Updates.combine(
            Updates.set(Artifact::name.name, artifact.name),
            Updates.set(Artifact::status.name, artifact.status),
            Updates.set(Artifact::conclusionDateTime.name, artifact.conclusionDateTime),
            Updates.set(Artifact::priority.name, artifact.priority)
        )

        val result = database.getCollection<Artifact>(COLLECTION).updateOne(query, updates)

        return result.modifiedCount > 0
    }

    companion object {
        private const val COLLECTION = "artifacts"
    }
}