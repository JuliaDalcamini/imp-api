package com.julia.imp.team

import com.julia.imp.common.db.CrudRepository
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class TeamRepository(database: MongoDatabase) : CrudRepository<Team>() {

    override val collection: MongoCollection<Team> = database.getCollection<Team>("teams")

}