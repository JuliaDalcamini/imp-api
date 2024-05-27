package com.julia.imp.team.member

import com.julia.imp.auth.user.UserRepository
import com.julia.imp.common.db.error.DuplicateItemException
import com.julia.imp.common.db.error.ItemNotFoundException
import com.julia.imp.common.networking.error.ConflictError
import com.julia.imp.common.networking.error.UnauthorizedError
import io.ktor.server.plugins.NotFoundException
import org.bson.types.ObjectId

class TeamMemberService(
    private val repository: TeamMemberRepository,
    private val userRepository: UserRepository
) {

    suspend fun add(teamId: String, request: AddTeamMemberRequest, loggedUserId: String): String {
        if (!isUserAdmin(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team admins can add new team members")
        }

        val userToAdd = userRepository.findById(request.userId)
            ?: throw NotFoundException("User not found")

        try {
            return repository.insert(
                TeamMember(
                    id = ObjectId(),
                    userId = userToAdd.id.toString(),
                    teamId = teamId,
                    role = request.role
                )
            )
        } catch (error: DuplicateItemException) {
            throw ConflictError("Team member already exists")
        }
    }

    suspend fun update(teamId: String, memberId: String, request: UpdateTeamMemberRequest, loggedUserId: String) {
        if (!isUserAdmin(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team admins can update team members")
        }

        val oldTeamMember = repository.findById(memberId)

        if (oldTeamMember == null || oldTeamMember.teamId != teamId) {
            throw NotFoundException("Member not found")
        }

        repository.replaceById(
            id = oldTeamMember.id.toString(),
            item = oldTeamMember.copy(role = request.role)
        )
    }

    suspend fun remove(teamId: String, memberId: String, loggedUserId: String) {
        if (!isUserAdmin(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team admins can update team members")
        }

        try {
            repository.deleteById(memberId)
        } catch (error: ItemNotFoundException) {
            throw NotFoundException("Team member not found")
        }
    }

    private suspend fun isUserAdmin(loggedUserId: String, teamId: String): Boolean {
        return repository.isAdmin(loggedUserId, teamId)
    }
}