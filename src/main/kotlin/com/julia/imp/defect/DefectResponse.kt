package com.julia.imp.defect

import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactReference
import com.julia.imp.defecttype.DefectType
import com.julia.imp.defecttype.DefectTypeResponse
import com.julia.imp.question.Severity
import kotlinx.serialization.Serializable

@Serializable
data class DefectResponse(
    val id: String,
    val type: DefectTypeResponse,
    val artifact: ArtifactReference,
    val severity: Severity,
    val description: String?,
    val fixed: Boolean
) {

    companion object {

        fun of(defect: Defect, defectType: DefectType, artifact: Artifact) = DefectResponse(
            id = defect.id.toString(),
            type = DefectTypeResponse.of(defectType),
            artifact = ArtifactReference.of(artifact),
            severity = defect.severity,
            description = defect.description,
            fixed = defect.fixed
        )
    }
}
