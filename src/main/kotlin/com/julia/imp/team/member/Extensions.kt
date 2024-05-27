package com.julia.imp.team.member

suspend fun TeamMemberRepository.getUserRole(userId: String, teamId: String): Role? =
    findByUserIdAndTeamId(userId, teamId)?.role

suspend fun TeamMemberRepository.isAdmin(userId: String, teamId: String): Boolean =
    getUserRole(userId, teamId) == Role.Admin

suspend fun TeamMemberRepository.isMember(userId: String, teamId: String): Boolean =
    findByUserIdAndTeamId(userId, teamId) != null