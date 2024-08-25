package com.julia.imp.artifact.type

import com.julia.imp.common.db.CrudRepository
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class ArtifactTypeRepository(database: MongoDatabase) : CrudRepository<ArtifactType>() {

    override val collection: MongoCollection<ArtifactType> = database.getCollection("artifactTypes")
}