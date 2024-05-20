package com.julia.imp.common.db

import com.julia.imp.common.db.error.ItemNotFoundException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import java.io.IOException

abstract class CrudRepository<T : Any> {

    protected abstract val collection: MongoCollection<T>

    open suspend fun insert(item: T): String {
        val result = collection.insertOne(item)
        return result.insertedId?.asObjectId()?.value?.toString() ?: throw IOException("Failed to insert item")
    }

    open suspend fun findById(id: String): T? =
        collection.find(Filters.eq("_id", ObjectId(id))).firstOrNull()

    open suspend fun findAll(): List<T> =
        collection.find().toList()

    open suspend fun replaceById(id: String, item: T) {
        val query = Filters.eq("_id", ObjectId(id))
        val result = collection.replaceOne(query, item)

        if (result.modifiedCount < 1) {
            throw ItemNotFoundException()
        }
    }

    open suspend fun deleteById(id: String) {
        val result = collection.deleteOne(Filters.eq("_id", ObjectId(id)))

        if (result.deletedCount < 1) {
            throw ItemNotFoundException()
        }
    }
}