package com.julia.imp.artifact.type

class ArtifactTypeService(
    private val repository: ArtifactTypeRepository
) {

    suspend fun getAll(): List<ArtifactTypeResponse> {
        val types = repository.findAll()
        return types.map { type -> ArtifactTypeResponse.of(type) }
    }
}
