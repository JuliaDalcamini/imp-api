package com.julia.imp.team.member

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class TeamMemberRepository(private var database: MongoDatabase) {

    suspend fun insertOne(team: TeamMember): String? {
        val result = database.getCollection<TeamMember>(COLLECTION).insertOne(team)
        return result.insertedId?.asObjectId()?.value?.toString()
    }

    suspend fun deleteById(id: ObjectId): Boolean {
        val result = database.getCollection<TeamMember>(COLLECTION).deleteOne(Filters.eq("_id", id))
        return result.deletedCount > 0
    }

    suspend fun findById(objectId: ObjectId): TeamMember? =
        database.getCollection<TeamMember>(COLLECTION)
            .find(Filters.eq("_id", objectId))
            .firstOrNull()

    suspend fun findByTeam(teamId: String): TeamMember? =
        database.getCollection<TeamMember>(COLLECTION)
            .find(Filters.eq("teamId", teamId))
            .firstOrNull()

    suspend fun updateOne(id: ObjectId, team: TeamMember): Boolean {
        val query = Filters.eq("_id", id)

        val updates = Updates.combine(
            Updates.set(TeamMember::role.name, team.role)
        )

        val result = database.getCollection<TeamMember>(COLLECTION).updateOne(query, updates)

        return result.modifiedCount > 0
    }

    companion object {
        private const val COLLECTION = "teams"
    }
}