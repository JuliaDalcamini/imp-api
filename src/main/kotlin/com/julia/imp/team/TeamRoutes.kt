package com.julia.imp.team

import com.julia.imp.team.create.createTeamRoute
import com.julia.imp.team.delete.deleteTeamRoute
import com.julia.imp.team.update.updateTeamRoute
import io.ktor.server.routing.Route

fun Route.teamRoutes() {
    createTeamRoute()
    updateTeamRoute()
    deleteTeamRoute()
}