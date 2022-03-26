package fourtune.merron.data.repository

import forutune.meeron.domain.model.Teams
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.TeamRepository
import fourtune.merron.data.model.dto.request.TeamRequest
import fourtune.merron.data.source.remote.TeamApi
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val teamApi: TeamApi,
) : TeamRepository {
    override suspend fun getTeams(workspaceId: Long): Teams {
        return teamApi.getTeams(workspaceId)
    }

    override suspend fun createTeam(workspaceId: Long, teamName: String): Long {
        return teamApi.createTeam(TeamRequest(workspaceId, teamName)).createdTeamId
    }

    override suspend fun getTeamMembers(teamId: Long): List<WorkspaceUser> {
        return teamApi.getTeamMembers(teamId).workspaceUsers
    }

}