package com.julia.imp.team.member

import com.julia.imp.auth.user.UserRepository
import com.julia.imp.common.db.error.DuplicateItemException
import com.julia.imp.common.db.error.ItemNotFoundException
import com.julia.imp.common.networking.error.ConflictError
import com.julia.imp.common.networking.error.UnauthorizedError
import io.ktor.server.plugins.NotFoundException

class TeamMemberService(
    private val repository: TeamMemberRepository,
    private val userRepository: UserRepository
) {

    suspend fun get(teamId: String, loggedUserId: String): List<TeamMemberResponse> {
        if (!repository.isMember(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team members can see other team members")
        }

        val teamMembers = repository.findByTeamId(teamId)

        return teamMembers.map { member ->
            val user = userRepository.findById(member.userId)
                ?: throw NotFoundException("Member not found")

            TeamMemberResponse.of(member, user)
        }
    }

    suspend fun get(teamId: String, userId: String, loggedUserId: String): TeamMemberResponse {
        if (!repository.isMember(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team members can see other team members")
        }

        val teamMember = repository.findByUserIdAndTeamId(userId, teamId)
            ?: throw NotFoundException("Member not found")

        val user = userRepository.findById(teamMember.userId)
            ?: throw NotFoundException("Member not found")

        return TeamMemberResponse.of(teamMember, user)
    }

    suspend fun add(teamId: String, request: AddTeamMemberRequest, loggedUserId: String): String {
        if (!repository.isAdmin(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team admins can add new team members")
        }

        val userToAdd = userRepository.findByEmail(request.email)
            ?: throw NotFoundException("User not found")

        try {
            return repository.insert(
                TeamMember(
                    userId = userToAdd.id.toString(),
                    teamId = teamId,
                    role = request.role
                )
            )
        } catch (error: DuplicateItemException) {
            throw ConflictError("Team member already exists")
        }
    }

    suspend fun update(teamId: String, userId: String, request: UpdateTeamMemberRequest, loggedUserId: String) {
        if (!repository.isAdmin(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team admins can update team members")
        }

        val oldTeamMember = repository.findByUserIdAndTeamId(userId, teamId)
            ?: throw NotFoundException("Member not found")

        repository.replaceById(
            id = oldTeamMember.id.toString(),
            item = oldTeamMember.copy(role = request.role)
        )
    }

    suspend fun remove(teamId: String, userId: String, loggedUserId: String) {
        if (!repository.isAdmin(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team admins can update team members")
        }

        try {
            repository.deleteByUserIdAndTeamId(userId, teamId)
        } catch (error: ItemNotFoundException) {
            throw NotFoundException("Team member not found")
        }
    }
}