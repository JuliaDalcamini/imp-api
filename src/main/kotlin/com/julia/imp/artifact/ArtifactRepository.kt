package com.julia.imp.artifact

import com.julia.imp.common.db.CrudRepository
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class ArtifactRepository(database: MongoDatabase) : CrudRepository<Artifact>() {

    override val collection: MongoCollection<Artifact> = database.getCollection("artifacts")
}