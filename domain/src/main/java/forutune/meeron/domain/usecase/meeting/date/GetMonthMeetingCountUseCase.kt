package forutune.meeron.domain.usecase.meeting.date

import forutune.meeron.domain.model.CalendarType
import forutune.meeron.domain.model.MonthCount
import forutune.meeron.domain.repository.MeetingRepository
import javax.inject.Inject

class GetMonthMeetingCountUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(type: CalendarType, year: Int): List<MonthCount> {
        val myWorkspaceUserId = 3L
        return meetingRepository.getMonthMeetingCount(type, myWorkspaceUserId, year)
    }
}