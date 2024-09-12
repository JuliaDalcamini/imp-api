package com.julia.imp.project

import com.julia.imp.common.db.CrudRepository
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class ProjectRepository(database: MongoDatabase) : CrudRepository<Project>() {

    override val collection: MongoCollection<Project> = database.getCollection("projects")

    suspend fun findByTeamId(teamId: String): List<Project> =
        collection
            .find(Filters.and(Filters.eq("teamId", teamId)))
            .toList()

    suspend fun findFiltered(teamId: String, filter: ProjectFilter) =
        when (filter) {
            ProjectFilter.Active -> findActiveByTeamId(teamId)
            ProjectFilter.Finished -> findFinishedByTeamId(teamId)
            ProjectFilter.All -> findByTeamId(teamId)
        }

    private suspend fun findActiveByTeamId(teamId: String): List<Project> =
        collection
            .find(
                Filters.and(
                    Filters.eq("teamId", teamId),
                    Filters.ne("finished", true)
                )
            )
            .toList()

    private suspend fun findFinishedByTeamId(teamId: String): List<Project> =
        collection
            .find(
                Filters.and(
                    Filters.eq("teamId", teamId),
                    Filters.eq("finished", true)
                )
            )
            .toList()

    suspend fun deleteAllByTeamId(id: String): Long {
        val result = collection.deleteMany(Filters.eq("teamId", id))
        return result.deletedCount
    }
}