package forutune.meeron.domain.usecase.meeting

import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.repository.MeetingRepository
import forutune.meeron.domain.repository.WorkSpaceRepository
import forutune.meeron.domain.repository.WorkspaceUserRepository
import javax.inject.Inject

class GetTodayMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val workSpaceRepository: WorkSpaceRepository
) {
    suspend operator fun invoke(): List<Meeting> {
        val workSpaceUserId = requireNotNull(workspaceUserRepository.getCurrentWorkspaceUserId())
        val workspaceId = workSpaceRepository.getCurrentWorkspaceId()
        return meetingRepository.getTodayMeetings(workspaceId, workSpaceUserId)
    }
}