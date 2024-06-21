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
}