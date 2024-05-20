package com.julia.imp.artifact

import com.julia.imp.common.db.error.ItemNotFoundException
import io.ktor.server.plugins.NotFoundException
import kotlinx.datetime.Clock
import org.bson.types.ObjectId

class ArtifactService(
    private val repository: ArtifactRepository
) {

    suspend fun create(request: CreateArtifactRequest, loggedUserId: String): String {
        // TODO: Verificar role do usuário

        return repository.insert(
            Artifact(
                id = ObjectId(),
                name = request.name,
                artifactTypeId = request.artifactTypeId,
                projectId = request.projectId,
                creatorId = loggedUserId,
                inspectorIds = request.inspectorIds,
                creationDateTime = Clock.System.now(),
                priority = request.priority,
            )
        )
    }

    suspend fun update(artifactId: String, request: UpdateArtifactRequest, loggedUserId: String) {
        val oldArtifact = repository.findById(artifactId)
            ?: throw NotFoundException("Artifact not found")

        // TODO: Verificar role do usuário

        repository.replaceById(
            id = oldArtifact.id.toString(),
            item = oldArtifact.copy(
                name = request.name,
                artifactTypeId = request.artifactTypeId,
                inspectorIds = request.inspectorIds
            )
        )
    }

    suspend fun delete(artifactId: String, loggedUserId: String) {
        // TODO: Verificar role do usuário

        try {
            repository.deleteById(artifactId)
        } catch (error: ItemNotFoundException) {
            throw NotFoundException("Artifact not found")
        }
    }
}