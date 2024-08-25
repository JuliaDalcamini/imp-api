package com.julia.imp.defecttype

import com.julia.imp.common.db.CrudRepository
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class DefectTypeRepository(database: MongoDatabase) : CrudRepository<DefectType>() {

    override val collection: MongoCollection<DefectType> = database.getCollection("defectTypes")
}