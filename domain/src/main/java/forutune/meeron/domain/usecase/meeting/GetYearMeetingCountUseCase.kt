package forutune.meeron.domain.usecase.meeting

import forutune.meeron.domain.model.CalendarType
import forutune.meeron.domain.model.YearCount
import forutune.meeron.domain.preference.MeeronPreference
import forutune.meeron.domain.repository.MeetingRepository
import javax.inject.Inject

class GetYearMeetingCountUseCase @Inject constructor(
    private val meeronPreference: MeeronPreference,
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(type: CalendarType): List<YearCount> {
        val myWorkspaceUserId = 3L
//            meeronPreference.getCurrentWorkSpaceUserId()
        return meetingRepository.getYearMeetingCount(type, myWorkspaceUserId)
    }
}