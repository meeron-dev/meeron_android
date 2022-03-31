package forutune.meeron.domain.usecase.team

import forutune.meeron.domain.model.Team
import javax.inject.Inject

class GetTeamUseCase @Inject constructor(
    private val getWorkSpaceTeam: GetWorkSpaceTeamUseCase
) {
    suspend operator fun invoke(teamId: Long): Team {
        val workspaceTeam = getWorkSpaceTeam()
        return workspaceTeam.first { it.id == teamId }
    }
}