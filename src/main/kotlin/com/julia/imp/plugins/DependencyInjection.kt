package com.julia.imp.plugins

import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.artifact.ArtifactService
import com.julia.imp.artifact.type.ArtifactTypeRepository
import com.julia.imp.artifact.type.ArtifactTypeService
import com.julia.imp.auth.AuthService
import com.julia.imp.auth.refresh.RefreshTokenRepository
import com.julia.imp.auth.user.UserRepository
import com.julia.imp.defect.DefectRepository
import com.julia.imp.defect.DefectService
import com.julia.imp.defecttype.DefectTypeRepository
import com.julia.imp.inspection.InspectionRepository
import com.julia.imp.inspection.InspectionService
import com.julia.imp.inspection.answer.InspectionAnswerRepository
import com.julia.imp.project.ProjectRepository
import com.julia.imp.project.ProjectService
import com.julia.imp.project.dashboard.DashboardService
import com.julia.imp.question.QuestionRepository
import com.julia.imp.question.QuestionService
import com.julia.imp.team.TeamRepository
import com.julia.imp.team.TeamService
import com.julia.imp.team.inspector.InspectorService
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.TeamMemberService
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(
            module {
                single<MongoClient> { MongoClient.create("mongodb://localhost:27017") }
                single<MongoDatabase> { get<MongoClient>().getDatabase("imp-db") }
            },
            module {
                single<UserRepository> { UserRepository(get()) }
                single<RefreshTokenRepository> { RefreshTokenRepository(get()) }
                single<AuthService> { AuthService(get(), get()) }
                single<ArtifactRepository> { ArtifactRepository(get()) }
                single<ArtifactService> { ArtifactService(get(), get(), get(), get(), get(), get()) }
                single<ArtifactTypeRepository> { ArtifactTypeRepository(get()) }
                single<ArtifactTypeService> { ArtifactTypeService(get()) }
                single<ProjectRepository> { ProjectRepository(get()) }
                single<ProjectService> { ProjectService(get(), get(), get(), get(), get(), get()) }
                single<TeamRepository> { TeamRepository(get()) }
                single<TeamService> { TeamService(get(), get(), get(), get(), get(), get()) }
                single<TeamMemberRepository> { TeamMemberRepository(get()) }
                single<TeamMemberService> { TeamMemberService(get(), get(), get()) }
                single<InspectorService> { InspectorService(get(), get()) }
                single<DefectTypeRepository> { DefectTypeRepository(get()) }
                single<ArtifactTypeRepository> { ArtifactTypeRepository(get()) }
                single<QuestionRepository> { QuestionRepository(get()) }
                single<QuestionService> { QuestionService(get(), get()) }
                single<DashboardService> { DashboardService(get(), get(), get(), get(), get(), get(), get(), get()) }
                single<DefectRepository> { DefectRepository(get()) }
                single<DefectService> { DefectService(get(), get(), get(), get(), get()) }
                single<InspectionRepository> { InspectionRepository(get()) }
                single<InspectionAnswerRepository> { InspectionAnswerRepository(get()) }

                single<InspectionService> {
                    InspectionService(get(), get(), get(), get(), get(), get(), get(), get(), get())
                }
            }
        )
    }
}