package com.julia.imp.inspection

import com.julia.imp.common.db.CrudRepository
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class InspectionRepository(database: MongoDatabase) : CrudRepository<Inspection>() {

    override val collection: MongoCollection<Inspection> = database.getCollection("inspections")

    suspend fun findByArtifactId(artifactId: String): List<Inspection> =
        collection
            .find(Filters.and(Filters.eq("artifactId", artifactId)))
            .toList()
}