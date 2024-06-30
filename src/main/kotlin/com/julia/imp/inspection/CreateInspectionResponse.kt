package com.julia.imp.inspection

import kotlinx.serialization.Serializable

@Serializable
data class CreateInspectionResponse(
    val inspectionId: String
)
