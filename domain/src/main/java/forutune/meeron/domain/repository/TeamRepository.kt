package forutune.meeron.domain.repository

import forutune.meeron.domain.model.Teams
import forutune.meeron.domain.model.WorkspaceUser

interface TeamRepository {
    suspend fun getTeams(workspaceId: Long): Teams
    suspend fun createTeam(workspaceId: Long, teamName: String): Long
    suspend fun getTeamMembers(teamId: Long): List<WorkspaceUser>
}