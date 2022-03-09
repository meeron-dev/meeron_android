package fourtune.merron.data.repository

import forutune.meeron.domain.model.Teams
import forutune.meeron.domain.repository.TeamRepository
import fourtune.merron.data.source.remote.TeamApi
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val teamApi: TeamApi,
) : TeamRepository {
    override suspend fun getTeams(workspaceId: Long): Teams {
        return teamApi.getTeams(workspaceId)
    }

}