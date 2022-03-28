package forutune.meeron.domain.usecase.meeting

import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.repository.MeetingRepository
import forutune.meeron.domain.usecase.workspace.GetLatestWorkspaceIdUseCase
import javax.inject.Inject

class CreateMeetingUseCase @Inject constructor(
    private val getLatestWorkspaceId: GetLatestWorkspaceIdUseCase,
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meeting: Meeting) {
        val workspaceId = getLatestWorkspaceId()
        meetingRepository.createMeeting(workspaceId, meeting)
    }
}