package com.julia.imp.inspection.answer

import com.julia.imp.common.db.CrudRepository
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class InspectionAnswerRepository(database: MongoDatabase) : CrudRepository<InspectionAnswer>() {

    override val collection: MongoCollection<InspectionAnswer> = database.getCollection("inspectionAnswers")

    suspend fun findByInspectionId(inspectionId: String): List<InspectionAnswer> =
        collection
            .find(Filters.eq("inspectionId", inspectionId))
            .toList()
}