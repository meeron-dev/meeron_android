package forutune.meeron.domain.usecase.meeting

import forutune.meeron.domain.model.CalendarType
import forutune.meeron.domain.model.YearCount
import forutune.meeron.domain.repository.MeetingRepository
import forutune.meeron.domain.usecase.me.GetMeUseCase
import forutune.meeron.domain.usecase.workspace.GetLatestWorkspaceIdUseCase
import java.util.*
import javax.inject.Inject

class GetYearMeetingCountUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val getLatestWorkspaceIdUseCase: GetLatestWorkspaceIdUseCase,
    private val getMe: GetMeUseCase,
    private val calendar: Calendar
) {
    suspend operator fun invoke(type: CalendarType): List<YearCount> {
        val id = when (type) {
            CalendarType.WORKSPACE -> getLatestWorkspaceIdUseCase()
            CalendarType.TEAM -> 0L//todo 나중에 팀
            CalendarType.WORKSPACE_USER -> getMe().userId
        }
        val yearCounts = meetingRepository.getYearMeetingCount(type, id)
        return yearCounts.ifEmpty { listOf(YearCount(calendar.get(Calendar.YEAR), 0)) }
    }
}