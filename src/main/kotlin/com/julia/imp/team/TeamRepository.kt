package com.julia.imp.team

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class TeamRepository(private var database: MongoDatabase) {

    suspend fun insertOne(team: Team): String {
        val result = database.getCollection<Team>(COLLECTION).insertOne(team)
        return result.insertedId?.asObjectId()?.value?.toString() ?: throw IOException("Failed to create team")
    }

    suspend fun deleteById(id: ObjectId): Boolean {
        val result = database.getCollection<Team>(COLLECTION).deleteOne(Filters.eq("_id", id))
        return result.deletedCount > 0
    }

    suspend fun findById(objectId: ObjectId): Team? =
        database.getCollection<Team>(COLLECTION)
            .find(Filters.eq("_id", objectId))
            .firstOrNull()

    suspend fun updateOne(id: ObjectId, team: Team): Boolean {
        val query = Filters.eq("_id", id)

        val updates = Updates.combine(
            Updates.set(Team::name.name, team.name)
        )

        val result = database.getCollection<Team>(COLLECTION).updateOne(query, updates)

        return result.modifiedCount > 0
    }

    companion object {
        private const val COLLECTION = "teams"
    }
}