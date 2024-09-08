package com.julia.imp.inspection

import com.julia.imp.common.db.CrudRepository
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class InspectionRepository(database: MongoDatabase) : CrudRepository<Inspection>() {

    override val collection: MongoCollection<Inspection> = database.getCollection("inspections")

    suspend fun findByArtifactId(artifactId: String): List<Inspection> =
        collection
            .find(Filters.and(Filters.eq("artifactId", artifactId)))
            .sort(Sorts.descending("createdAt"))
            .toList()

    suspend fun findByArtifactId(artifactId: ObjectId): List<Inspection> =
        findByArtifactId(artifactId.toString())
}