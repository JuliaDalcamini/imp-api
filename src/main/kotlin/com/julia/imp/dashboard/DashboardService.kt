package com.julia.imp.dashboard

import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.inspection.InspectionRepository
import com.julia.imp.project.ProjectRepository

class DashboardService(
    private val projectRepository: ProjectRepository,
    private val artifactRepository: ArtifactRepository,
    private val inspectionRepository: InspectionRepository
) {

    fun get(projectId: String, loggedUserId: String) {

    }
}