package com.julia.imp.defect.type

import kotlinx.serialization.Serializable

@Serializable
data class DefectTypeResponse(
    val id: String,
    val name: String
) {

    companion object {

        fun of(defectType: DefectType) = DefectTypeResponse(
            id = defectType.id.toString(),
            name = defectType.name
        )
    }
}
