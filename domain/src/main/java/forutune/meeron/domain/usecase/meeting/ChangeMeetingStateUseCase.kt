package forutune.meeron.domain.usecase.meeting

import forutune.meeron.domain.model.MeetingState
import forutune.meeron.domain.repository.MeetingRepository
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import javax.inject.Inject

class ChangeMeetingStateUseCase @Inject constructor(
    private val getMyWorkspaceUser: GetMyWorkSpaceUserUseCase,
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meetingId: Long, state: MeetingState) {
        val myWorkspaceUser = getMyWorkspaceUser()
        meetingRepository.changeMeetingState(myWorkspaceUser.workspaceUserId, meetingId, state)
    }
}