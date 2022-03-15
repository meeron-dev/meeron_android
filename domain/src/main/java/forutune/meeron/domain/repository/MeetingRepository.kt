package forutune.meeron.domain.repository

import forutune.meeron.domain.model.CalendarType
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.MonthCount
import forutune.meeron.domain.model.YearCount

interface MeetingRepository {
    suspend fun createMeeting(workSpaceId: Long, meeting: Meeting)
    suspend fun getTodayMeetings(workSpaceId: Long, workSpaceUserId: Long): List<Meeting>
    suspend fun getYearMeetingCount(type: CalendarType, id: Long): List<YearCount>
    suspend fun getMonthMeetingCount(type: CalendarType, id: Long, year: Int): List<MonthCount>
}