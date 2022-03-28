package forutune.meeron.domain.usecase.team

import forutune.meeron.domain.model.Team
import forutune.meeron.domain.repository.TeamRepository
import forutune.meeron.domain.repository.WorkSpaceRepository
import javax.inject.Inject

class GetWorkSpaceTeamUseCase @Inject constructor(
    private val teamRepository: TeamRepository,
    private val workSpaceRepository: WorkSpaceRepository
) {
    suspend operator fun invoke(): List<Team> {
        val workspaceId = workSpaceRepository.getCurrentWorkspaceId()
        return teamRepository.getTeams(workspaceId).teams
    }
}