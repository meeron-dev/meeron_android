package forutune.meeron.domain.usecase.meeting.team

import forutune.meeron.domain.model.MeetingState
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.MeetingRepository
import javax.inject.Inject

class GetTeamMemberInMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meetingId: Long, teamId: Long): Map<MeetingState, List<WorkspaceUser>> {
        return meetingRepository.getTeamMember(meetingId = meetingId, teamId = teamId)
    }
}