package forutune.meeron.domain.usecase.meeting

import forutune.meeron.domain.model.CalendarType
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.repository.MeetingRepository
import forutune.meeron.domain.repository.UserRepository
import javax.inject.Inject

class GetDateMeetingCountUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(type: CalendarType, date: Date): List<Int> {
        val myWorkspaceUserId = 3L
//        userRepository.getCurrentWorkspaceUserId().firstOrNull()
        return meetingRepository.getDateMeetingCount(type, myWorkspaceUserId, date)
    }
}