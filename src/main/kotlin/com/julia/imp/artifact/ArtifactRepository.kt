package com.julia.imp.artifact

import com.julia.imp.common.db.CrudRepository
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class ArtifactRepository(database: MongoDatabase) : CrudRepository<Artifact>() {

    override val collection: MongoCollection<Artifact> = database.getCollection("artifacts")

    suspend fun findByProjectId(projectId: String): List<Artifact> =
        collection
            .find(Filters.and(Filters.eq("projectId", projectId)))
            .toList()
}