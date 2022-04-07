package forutune.meeron.domain.usecase.meeting

import forutune.meeron.domain.repository.MeetingRepository
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import javax.inject.Inject

class DeleteMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val myWorkSpaceUser: GetMyWorkSpaceUserUseCase
) {
    suspend operator fun invoke(meetingId: Long) {
        val workspaceUser = myWorkSpaceUser()
        meetingRepository.deleteMeeting(meetingId = meetingId, workspaceUserId = workspaceUser.workspaceUserId)
    }
}