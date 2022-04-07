package forutune.meeron.domain.usecase.meeting.date

import forutune.meeron.domain.model.CalendarType
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.repository.MeetingRepository
import forutune.meeron.domain.usecase.me.GetMeUseCase
import forutune.meeron.domain.usecase.workspace.GetLatestWorkspaceIdUseCase
import javax.inject.Inject

class GetDateMeetingCountUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val getLatestWorkspaceIdUseCase: GetLatestWorkspaceIdUseCase,
    private val getMe: GetMeUseCase
) {
    suspend operator fun invoke(type: CalendarType, date: Date): List<Int> {
        val id = when (type) {
            CalendarType.WORKSPACE -> getLatestWorkspaceIdUseCase()
            CalendarType.TEAM -> 0L//todo 나중에 팀
            CalendarType.WORKSPACE_USER -> getMe().userId
        }
        return meetingRepository.getDateMeetingCount(type, id, date)
    }
}