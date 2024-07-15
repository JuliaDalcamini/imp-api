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

    suspend fun findFiltered(projectId: String, userId: String, filter: ArtifactFilter): List<Artifact> =
        when (filter) {
            ArtifactFilter.Active -> findActiveByProjectId(projectId)
            ArtifactFilter.AssignedToMe -> findAssignedByProjectId(projectId, userId)
            ArtifactFilter.Archived -> findArchivedByProjectId(projectId)
            ArtifactFilter.All -> findByProjectId(projectId)
        }

    private suspend fun findActiveByProjectId(projectId: String): List<Artifact> =
        collection
            .find(
                Filters.and(
                    Filters.eq("projectId", projectId),
                    Filters.ne("archived", true)
                )
            )
            .toList()

    private suspend fun findAssignedByProjectId(projectId: String, userId: String) =
        collection
            .find(
                Filters.and(
                    Filters.eq("projectId", projectId),
                    Filters.all("inspectorIds", userId)
                )
            )
            .toList()

    private suspend fun findArchivedByProjectId(projectId: String): List<Artifact> =
        collection
            .find(
                Filters.and(
                    Filters.eq("projectId", projectId),
                    Filters.eq("archived", true)
                )
            )
            .toList()
}