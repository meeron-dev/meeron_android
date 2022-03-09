package forutune.meeron.domain.usecase

import forutune.meeron.domain.model.Team
import forutune.meeron.domain.repository.TeamRepository
import javax.inject.Inject

class GetWorkSpaceTeamUseCase @Inject constructor(
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(): List<Team> {
        return teamRepository.getTeams(1).teams
    }
}