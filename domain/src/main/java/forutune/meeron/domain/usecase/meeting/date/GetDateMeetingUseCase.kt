package forutune.meeron.domain.usecase.meeting.date

import forutune.meeron.domain.model.CalendarType
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.repository.MeetingRepository
import javax.inject.Inject

class GetDateMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(type: CalendarType, date: Date): List<Pair<Meeting, WorkSpaceInfo?>> {
        val myWorkspaceUserId = 3L
//            meeronPreference.getCurrentWorkSpaceUserId()
        return meetingRepository.getDateMeeting(type, myWorkspaceUserId, date)
    }
}