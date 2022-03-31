package forutune.meeron.domain.usecase.team

import forutune.meeron.domain.repository.TeamRepository
import forutune.meeron.domain.usecase.workspace.GetLatestWorkspaceIdUseCase
import javax.inject.Inject

class CreateTeamUseCase @Inject constructor(
    private val getLatestWorkspaceId: GetLatestWorkspaceIdUseCase,
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(teamName: String) {
        val workspaceId = getLatestWorkspaceId()
        teamRepository.createTeam(workspaceId, teamName)
    }
}