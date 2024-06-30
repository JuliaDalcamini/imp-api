package com.julia.imp.inspection

import com.julia.imp.auth.user.User
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class InspectionResponse(
    val id: String,
    val inspector: Inspector,
    val duration: Duration,
    val lastUpdate: Instant
) {

    @Serializable
    data class Inspector(
        val id: String,
        val firstName: String,
        val lastName: String
    )

    companion object {

        fun of(inspection: Inspection, inspector: User) = InspectionResponse(
            id = inspection.id.toString(),
            inspector = Inspector(
                id = inspector.id.toString(),
                firstName = inspector.firstName,
                lastName = inspector.lastName
            ),
            duration = inspection.duration,
            lastUpdate = inspection.lastUpdate
        )
    }
}