package com.julia.imp.artifact

import com.julia.imp.artifact.create.createArtifactRoute
import com.julia.imp.artifact.delete.deleteArtifactRoute
import com.julia.imp.artifact.update.updateArtifactRoute
import io.ktor.server.routing.*

fun Route.artifactRoutes() {
    createArtifactRoute()
    deleteArtifactRoute()
    updateArtifactRoute()
}

