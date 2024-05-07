package com.julia.imp.plugins

import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.auth.user.UserRepository
import com.julia.imp.project.ProjectRepository
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
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
                single<ArtifactRepository> { ArtifactRepository(get()) }
                single<ProjectRepository> { ProjectRepository(get()) }
            }
        )
    }
}