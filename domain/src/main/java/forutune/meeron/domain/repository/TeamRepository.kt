package forutune.meeron.domain.repository

import forutune.meeron.domain.model.Teams

interface TeamRepository {
    suspend fun getTeams(workspaceId: Long): Teams
    suspend fun createTeam(workspaceId: Long, teamName: String): Long
}