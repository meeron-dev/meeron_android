package forutune.meeron.domain.usecase.meeting

import forutune.meeron.domain.model.CalendarType
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.preference.MeeronPreference
import forutune.meeron.domain.repository.MeetingRepository
import javax.inject.Inject

class GetDateMeetingCountUseCase @Inject constructor(
    private val meeronPreference: MeeronPreference,
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(type: CalendarType, date: Date): List<Int> {
        val myWorkspaceUserId = 3L
//            meeronPreference.getCurrentWorkSpaceUserId()
        return meetingRepository.getDateMeetingCount(type, myWorkspaceUserId, date)
    }
}