package com.julia.imp.artifact

import com.mongodb.kotlin.client.coroutine.MongoDatabase

class ArtifactRepository(private var database: MongoDatabase) {

    suspend fun insertOne(artifact: Artifact): Boolean {
        val result = database.getCollection<Artifact>(COLLECTION).insertOne(artifact)
        return result.insertedId != null
    }

    companion object {
        private const val COLLECTION = "artifacts"
    }
}