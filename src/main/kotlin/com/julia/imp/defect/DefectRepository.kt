package com.julia.imp.defect

import com.julia.imp.common.db.CrudRepository
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class DefectRepository(database: MongoDatabase) : CrudRepository<Defect>() {

    override val collection: MongoCollection<Defect> = database.getCollection("defects")

    private suspend fun findByAnswerId(answerId: String): List<Defect> =
        collection
            .find(Filters.and(Filters.eq("answerId", answerId)))
            .toList()

    suspend fun findByAnswerId(answerId: ObjectId): Defect? =
        findByAnswerId(answerId.toString()).firstOrNull()

    suspend fun findByProjectId(projectId: String): List<Defect> =
        collection
            .find(Filters.and(Filters.eq("projectId", projectId)))
            .toList()

    suspend fun findFiltered(artifactId: String, filter: DefectFilter): List<Defect> =
        when (filter) {
            DefectFilter.NotFixed -> findNotFixedByArtifactId(artifactId)
            DefectFilter.Fixed -> findFixedByArtifactId(artifactId)
            DefectFilter.All -> findByProjectId(artifactId)
        }

    private suspend fun findNotFixedByArtifactId(artifactId: String) =
        collection
            .find(
                Filters.and(
                    Filters.eq("artifactId", artifactId),
                    Filters.eq("fixed", false)
                )
            )
            .toList()

    private suspend fun findFixedByArtifactId(artifactId: String): List<Defect> =
        collection
            .find(
                Filters.and(
                    Filters.eq("artifactId", artifactId),
                    Filters.eq("fixed", true)
                )
            )
            .toList()
}