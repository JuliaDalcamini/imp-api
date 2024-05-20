package com.julia.imp.auth.user

import com.julia.imp.common.db.CrudRepository
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull

class UserRepository(database: MongoDatabase) : CrudRepository<User>() {

    override val collection: MongoCollection<User> = database.getCollection("users")

    suspend fun findByEmail(email: String): User? =
        collection.find(Filters.eq("email", email)).firstOrNull()
}