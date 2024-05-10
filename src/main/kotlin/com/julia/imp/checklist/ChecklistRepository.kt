package com.julia.imp.checklist

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class ChecklistRepository(private var database: MongoDatabase) {

    suspend fun insertOne(checklist: ChecklistTemplate): String {
        val result = database.getCollection<Checklist>(COLLECTION).insertOne(checklist)
        return result.insertedId?.asObjectId()?.value?.toString() ?: throw IOException("Failed to create checklist")
    }

    suspend fun deleteById(id: ObjectId): Boolean {
        val result = database.getCollection<Checklist>(COLLECTION).deleteOne(Filters.eq("_id", id))
        return result.deletedCount > 0
    }

    suspend fun findById(objectId: ObjectId): Checklist? =
        database.getCollection<Checklist>(COLLECTION)
            .find(Filters.eq("_id", objectId))
            .firstOrNull()

    suspend fun updateOne(id: ObjectId, checklist: Checklist): Boolean {
        val query = Filters.eq("_id", id)

        val updates = Updates.combine(
            //Updates.set(Checklist::name.name, checklist.name)
        )

        val result = database.getCollection<Checklist>(COLLECTION).updateOne(query, updates)

        return result.modifiedCount > 0
    }

    companion object {
        private const val COLLECTION = "checklists"
    }
}