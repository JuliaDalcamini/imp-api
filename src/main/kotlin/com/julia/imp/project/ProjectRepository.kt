package com.julia.imp.project

import com.julia.imp.common.db.CrudRepository
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class ProjectRepository(database: MongoDatabase) : CrudRepository<Project>() {

    override val collection: MongoCollection<Project> = database.getCollection("projects")

}