package forutune.meeron.domain.repository

import forutune.meeron.domain.model.*

interface MeetingRepository {
    suspend fun createMeeting(workSpaceId: Long, meeting: Meeting)
    suspend fun getTodayMeetings(workSpaceId: Long, workSpaceUserId: Long): List<Meeting>
    suspend fun getYearMeetingCount(type: CalendarType, id: Long): List<YearCount>
    suspend fun getMonthMeetingCount(type: CalendarType, id: Long, year: Int): List<MonthCount>
    suspend fun getDateMeetingCount(type: CalendarType, id: Long, date: Date): List<Int>
    suspend fun getDateMeeting(type: CalendarType, id: Long, date: Date): List<Pair<Meeting, WorkSpaceInfo?>>
}