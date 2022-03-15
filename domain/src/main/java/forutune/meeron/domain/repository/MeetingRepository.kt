package forutune.meeron.domain.repository

import forutune.meeron.domain.model.*

interface MeetingRepository {
    suspend fun createMeeting(workSpaceId: Long, meeting: Meeting)
    suspend fun getTodayMeetings(workSpaceId: Long, workSpaceUserId: Long): List<Meeting>
    suspend fun getYearMeetingCount(type: CalendarType, id: Long): List<YearCount>
    suspend fun getMonthMeetingCount(type: CalendarType, id: Long, year: Int): List<MonthCount>
    suspend fun getDateMeetingCount(type: CalendarType, myWorkspaceUserId: Long, date: Date): List<Int>
}