package forutune.meeron.domain.usecase.meeting

import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.repository.MeetingRepository
import forutune.meeron.domain.repository.UserRepository
import forutune.meeron.domain.repository.WorkSpaceRepository
import javax.inject.Inject

class GetTodayMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val userRepository: UserRepository,
    private val workSpaceRepository: WorkSpaceRepository
) {
    suspend operator fun invoke(): List<Meeting> {
        val myId = userRepository.getUserId()
        val workspaceId = workSpaceRepository.getCurrentWorkspaceId()
        return meetingRepository.getTodayMeetings(requireNotNull(workspaceId), myId)//todo workspaceId
    }
}