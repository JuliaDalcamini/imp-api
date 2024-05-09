package com.julia.imp.teammember

import com.julia.imp.teammember.add.addTeamMemberRoute
import io.ktor.server.routing.Route

fun Route.teamMemberRoutes() {
    addTeamMemberRoute()
}
