package com.julia.imp.checklist

import com.julia.imp.common.db.CrudRepository
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class ChecklistRepository(database: MongoDatabase) : CrudRepository<Checklist>() {

    override val collection: MongoCollection<Checklist> = database.getCollection("checklists")
}