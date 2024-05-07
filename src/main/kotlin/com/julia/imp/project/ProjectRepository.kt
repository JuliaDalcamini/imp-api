package com.julia.imp.project

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class ProjectRepository(private var database: MongoDatabase) {

    suspend fun insertOne(project: Project): String? {
        val result = database.getCollection<Project>(COLLECTION).insertOne(project)
        return result.insertedId?.asObjectId()?.value?.toString()
    }

    suspend fun deleteById(id: ObjectId): Boolean {
        val result = database.getCollection<Project>(COLLECTION).deleteOne(Filters.eq("_id", id))
        return result.deletedCount > 0
    }

    suspend fun findById(objectId: ObjectId): Project? =
        database.getCollection<Project>(COLLECTION)
            .find(Filters.eq("_id", objectId))
            .firstOrNull()

    suspend fun updateOne(id: ObjectId, project: Project): Boolean {
        val query = Filters.eq("_id", id)

        val updates = Updates.combine(
            Updates.set(Project::name.name, project.name)
        )

        val result = database.getCollection<Project>(COLLECTION).updateOne(query, updates)

        return result.modifiedCount > 0
    }

    companion object {
        private const val COLLECTION = "projects"
    }
}