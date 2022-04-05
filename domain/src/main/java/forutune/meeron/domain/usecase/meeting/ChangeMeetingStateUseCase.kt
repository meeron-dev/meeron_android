package forutune.meeron.domain.usecase.meeting

import forutune.meeron.domain.model.MeetingState
import forutune.meeron.domain.repository.MeetingRepository
import forutune.meeron.domain.usecase.workspace.GetLatestWorkspaceIdUseCase
import javax.inject.Inject

class ChangeMeetingStateUseCase @Inject constructor(
    private val getWorkspaceUserId: GetLatestWorkspaceIdUseCase,
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meetingId: Long, state: MeetingState) {
        val workspaceUserId = getWorkspaceUserId()
        meetingRepository.changeMeetingState(workspaceUserId, meetingId, state)
    }
}