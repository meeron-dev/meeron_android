package forutune.meeron.domain.repository

import forutune.meeron.domain.model.Teams
import forutune.meeron.domain.model.WorkspaceUser

interface TeamRepository {
    suspend fun getTeams(workspaceId: Long): Teams
    suspend fun createTeam(workspaceId: Long, teamName: String): Long
    suspend fun getTeamMembers(teamId: Long): List<WorkspaceUser>
    suspend fun kickTeamMember(workspaceUserId: Long, adminWorkspaceUserId: Long)
    suspend fun deleteTeam(teamId: Long, workspaceUserId: Long)
    suspend fun addTeamMember(teamId: Long, adminWorkspaceUserId: Long, workspaceUserIds: List<Long>)
}