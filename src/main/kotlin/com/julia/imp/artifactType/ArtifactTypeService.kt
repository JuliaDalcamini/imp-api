package com.julia.imp.artifactType

class ArtifactTypeService(
    private val repository: ArtifactTypeRepository
) {

    suspend fun get(): List<ArtifactTypeResponse> {
        val types = repository.findAll()
        return types.map { type -> ArtifactTypeResponse.of(type) }
    }
}
