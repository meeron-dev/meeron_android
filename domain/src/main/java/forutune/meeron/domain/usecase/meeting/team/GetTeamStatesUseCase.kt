package forutune.meeron.domain.usecase.meeting.team

import forutune.meeron.domain.model.TeamState
import forutune.meeron.domain.repository.MeetingRepository
import javax.inject.Inject

class GetTeamStatesUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meetingId: Long): List<TeamState> {
        return meetingRepository.getTeamStates(meetingId)
    }
}