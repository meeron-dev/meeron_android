package forutune.meeron.domain.repository

import forutune.meeron.domain.model.*

interface MeetingRepository {
    suspend fun createMeeting(workSpaceId: Long, meeting: Meeting): Long
    suspend fun getTodayMeetings(workSpaceId: Long, workSpaceUserId: Long): List<Meeting>
    suspend fun getYearMeetingCount(type: CalendarType, id: Long): List<YearCount>
    suspend fun getMonthMeetingCount(type: CalendarType, id: Long, year: Int): List<MonthCount>
    suspend fun getDateMeetingCount(type: CalendarType, id: Long, date: Date): List<Int>
    suspend fun getDateMeeting(type: CalendarType, id: Long, date: Date): List<Pair<Meeting, WorkSpaceInfo?>>
    suspend fun addAgenda(meetingId: Long, meeting: Meeting): List<Long>
    suspend fun addParticipants(meetingId: Long, meeting: Meeting)
    suspend fun addFiles(agendaId: Long, fileInfo: FileInfo)

    suspend fun getTeamStates(meetingId: Long): List<TeamState>

    suspend fun getAgenda(meetingId: Long, agendaOrder: Int): Agenda
    suspend fun changeMeetingState(workSpaceUserId: Long, meetingId: Long, state: MeetingState)
    suspend fun getTeamMember(meetingId: Long, teamId: Long): Map<MeetingState, List<WorkspaceUser>>
    suspend fun getAgendaInfo(meetingId: Long): AgendaInfo
}