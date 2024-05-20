package com.julia.imp.team.member

import com.julia.imp.common.db.CrudRepository
import com.julia.imp.common.db.error.DuplicateItemException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull

class TeamMemberRepository(database: MongoDatabase) : CrudRepository<TeamMember>() {

    override val collection: MongoCollection<TeamMember> = database.getCollection("teamMembers")

    override suspend fun insert(item: TeamMember): String {
        val existingMember = findByUserIdAndTeamId(item.userId, item.teamId)

        if (existingMember != null) {
            throw DuplicateItemException("Member already exists")
        }

        return super.insert(item)
    }

    suspend fun findByUserIdAndTeamId(userId: String, teamId: String): TeamMember? =
        collection
            .find(Filters.and(Filters.eq("userId", userId), Filters.eq("teamId", teamId)))
            .firstOrNull()

    suspend fun deleteByTeamId(id: String): Long {
        val result = collection.deleteMany(Filters.eq("teamId", id))
        return result.deletedCount
    }
}