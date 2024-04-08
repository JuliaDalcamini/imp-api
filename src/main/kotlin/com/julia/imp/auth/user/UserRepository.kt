package com.julia.imp.auth.user

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class UserRepository(private var database: MongoDatabase) {

    suspend fun insertOne(user: User): Boolean {
        val result = database.getCollection<User>(COLLECTION).insertOne(user)
        return result.insertedId != null
    }

    suspend fun deleteById(id: ObjectId): Boolean {
        val result = database.getCollection<User>(COLLECTION).deleteOne(Filters.eq("_id", id))
        return result.deletedCount > 0
    }

    suspend fun findById(objectId: ObjectId): User? =
        database.getCollection<User>(COLLECTION)
            .find(Filters.eq("_id", objectId))
            .firstOrNull()

    suspend fun findByEmail(email: String): User? =
        database.getCollection<User>(COLLECTION)
            .find(Filters.eq("email", email))
            .firstOrNull()

    suspend fun updateOne(id: ObjectId, user: User): Boolean {
        val query = Filters.eq("_id", id)

        val updates = Updates.combine(
            Updates.set(User::firstName.name, user.firstName),
            Updates.set(User::lastName.name, user.lastName),
            Updates.set(User::email.name, user.email),
            Updates.set(User::password.name, user.password)
        )

        val result = database.getCollection<User>(COLLECTION).updateOne(query, updates)

        return result.modifiedCount > 0
    }

    companion object {
        private const val COLLECTION = "user"
    }
}