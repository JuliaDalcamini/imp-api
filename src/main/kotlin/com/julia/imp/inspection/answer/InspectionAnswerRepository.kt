package com.julia.imp.inspection.answer

import com.julia.imp.common.db.CrudRepository
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class InspectionAnswerRepository(database: MongoDatabase) : CrudRepository<InspectionAnswer>() {

    override val collection: MongoCollection<InspectionAnswer> = database.getCollection("inspectionAnswers")
}