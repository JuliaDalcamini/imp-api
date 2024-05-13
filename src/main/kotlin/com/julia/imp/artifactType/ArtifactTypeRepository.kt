package com.julia.imp.artifactType

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class ArtifactTypeRepository(private var database: MongoDatabase) {

    suspend fun insertOne(artifactType: ArtifactType): String {
        val result = database.getCollection<ArtifactType>(COLLECTION).insertOne(artifactType)
        return result.insertedId?.asObjectId()?.value?.toString() ?: throw IOException("Failed to create artifactType")
    }

    suspend fun deleteById(id: ObjectId): Boolean {
        val result = database.getCollection<ArtifactType>(COLLECTION).deleteOne(Filters.eq("_id", id))
        return result.deletedCount > 0
    }

    suspend fun findAllArtifactTypesIds(): List<String> = findAllArtifactTypes().map { it.id.toString() }

    suspend fun findAllArtifactTypes(): List<ArtifactType> =
        database.getCollection<ArtifactType>(COLLECTION)
            .find().toList()

    suspend fun findById(objectId: ObjectId): ArtifactType? =
        database.getCollection<ArtifactType>(COLLECTION)
            .find(Filters.eq("_id", objectId))
            .firstOrNull()

    suspend fun updateOne(id: ObjectId, artifactType: ArtifactType): Boolean {
        val query = Filters.eq("_id", id)

        val updates = Updates.combine(
            //Updates.set(ArtifactType::name.name, artifactType.name)
        )

        val result = database.getCollection<ArtifactType>(COLLECTION).updateOne(query, updates)

        return result.modifiedCount > 0
    }

    companion object {
        private const val COLLECTION = "artifactTypes"
    }
}