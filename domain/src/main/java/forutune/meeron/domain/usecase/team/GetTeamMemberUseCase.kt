package forutune.meeron.domain.usecase.team

import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.TeamRepository
import javax.inject.Inject

class GetTeamMemberUseCase @Inject constructor(
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(teamId: Long): List<WorkspaceUser> {
        return teamRepository.getTeamMembers(teamId)
    }
}