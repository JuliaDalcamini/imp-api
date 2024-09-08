package com.julia.imp.artifact

import com.julia.imp.common.db.CrudRepository
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class ArtifactRepository(database: MongoDatabase) : CrudRepository<Artifact>() {

    override val collection: MongoCollection<Artifact> = database.getCollection("artifacts")

    suspend fun findByProjectId(projectId: String): List<Artifact> =
        collection
            .find(Filters.and(Filters.eq("projectId", projectId)))
            .toList()

    suspend fun findByProjectId(projectId: ObjectId): List<Artifact> =
        findByProjectId(projectId.toString())

    suspend fun findFiltered(projectId: String, userId: String, filter: ArtifactFilter): List<Artifact> =
        when (filter) {
            ArtifactFilter.AssignedToMe -> findAssignedByProjectId(projectId, userId)
            ArtifactFilter.Prioritized -> findPrioritizedByProjectId(projectId)
            ArtifactFilter.NotPrioritized -> findNotPrioritizedByProjectId(projectId)
            ArtifactFilter.Archived -> findArchivedByProjectId(projectId)
            ArtifactFilter.All -> findByProjectId(projectId)
        }

    private suspend fun findAssignedByProjectId(projectId: String, userId: String) =
        collection
            .find(
                Filters.and(
                    Filters.eq("projectId", projectId),
                    Filters.ne("archived", true),
                    Filters.all("inspectorIds", userId)
                )
            )
            .toList()

    private suspend fun findPrioritizedByProjectId(projectId: String): List<Artifact> =
        collection
            .find(
                Filters.and(
                    Filters.eq("projectId", projectId),
                    Filters.ne("archived", true),
                    Filters.ne("priority", null)
                )
            )
            .toList()

    private suspend fun findNotPrioritizedByProjectId(projectId: String): List<Artifact> =
        collection
            .find(
                Filters.and(
                    Filters.eq("projectId", projectId),
                    Filters.ne("archived", true),
                    Filters.eq("priority", null)
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