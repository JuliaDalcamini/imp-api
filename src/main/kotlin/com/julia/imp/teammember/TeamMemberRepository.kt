package com.julia.imp.teammember

import com.julia.imp.common.db.error.DuplicateItemError
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class TeamMemberRepository(private var database: MongoDatabase) {

    suspend fun insertOne(teamMember: TeamMember): String {
        val existingMember = findByUserIdAndTeamId(teamMember.userId, teamMember.teamId)

        if (existingMember != null) {
            throw DuplicateItemError("Member already exists")
        }

        val result = database.getCollection<TeamMember>(COLLECTION).insertOne(teamMember)

        return result.insertedId?.asObjectId()?.value?.toString() ?: throw IOException("Failed to add team member")
    }

    suspend fun findById(objectId: ObjectId): TeamMember? =
        database.getCollection<TeamMember>(COLLECTION)
            .find(Filters.eq("_id", objectId))
            .firstOrNull()

    suspend fun findByUserIdAndTeamId(userId: String, teamId: String): TeamMember? =
        database.getCollection<TeamMember>(COLLECTION)
            .find(
                Filters.and(
                    Filters.eq("userId", userId),
                    Filters.eq("teamId", teamId)
                )
            )
            .firstOrNull()

    suspend fun findByUserId(userId: String): TeamMember? =
        database.getCollection<TeamMember>(COLLECTION)
            .find(Filters.eq("userId", userId))
            .firstOrNull()

    suspend fun updateOne(id: ObjectId, teamMember: TeamMember): Boolean {
        val query = Filters.eq("_id", id)

        val updates = Updates.combine(
            Updates.set(TeamMember::role.name, teamMember.role)
        )

        val result = database.getCollection<TeamMember>(COLLECTION).updateOne(query, updates)

        return result.modifiedCount > 0
    }

    suspend fun deleteById(id: ObjectId): Boolean {
        val result = database.getCollection<TeamMember>(COLLECTION).deleteOne(Filters.eq("_id", id))
        return result.deletedCount > 0
    }

    suspend fun deleteByTeamId(id: ObjectId): Boolean {
        val result = database.getCollection<TeamMember>(COLLECTION).deleteMany(Filters.eq("teamId", id))
        return result.deletedCount > 0
    }

    companion object {
        private const val COLLECTION = "teamMembers"
    }
}