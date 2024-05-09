package com.julia.imp.teammember

import com.julia.imp.teammember.add.addTeamMemberRoute
import com.julia.imp.teammember.remove.removeTeamMemberRoute
import com.julia.imp.teammember.update.updateTeamMemberRoute
import io.ktor.server.routing.Route

fun Route.teamMemberRoutes() {
    addTeamMemberRoute()
    updateTeamMemberRoute()
    removeTeamMemberRoute()
}
