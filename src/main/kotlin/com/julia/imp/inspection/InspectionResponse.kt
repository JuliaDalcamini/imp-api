package com.julia.imp.inspection

import com.julia.imp.auth.user.User
import com.julia.imp.team.inspector.InspectorResponse
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class InspectionResponse(
    val id: String,
    val inspector: InspectorResponse,
    val duration: Duration,
    val lastUpdate: Instant
) {

    companion object {

        fun of(inspection: Inspection, inspector: User) = InspectionResponse(
            id = inspection.id.toString(),
            inspector = InspectorResponse.of(inspector),
            duration = inspection.duration,
            lastUpdate = inspection.lastUpdate
        )
    }
}