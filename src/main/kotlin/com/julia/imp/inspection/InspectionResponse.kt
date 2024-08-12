package com.julia.imp.inspection

import com.julia.imp.auth.user.User
import com.julia.imp.auth.user.UserResponse
import com.julia.imp.inspection.answer.InspectionAnswerResponse
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class InspectionResponse(
    val id: String,
    val inspector: UserResponse,
    val duration: Duration,
    val createdAt: Instant,
    val answers: List<InspectionAnswerResponse>
) {

    companion object {

        fun of(inspection: Inspection, inspector: User, answers: List<InspectionAnswerResponse>) = InspectionResponse(
            id = inspection.id.toString(),
            inspector = UserResponse.of(inspector),
            duration = inspection.duration,
            createdAt = inspection.createdAt,
            answers = answers
        )
    }
}