package fourtune.merron.data.repository

import forutune.meeron.domain.model.*
import forutune.meeron.domain.provider.FileProvider
import forutune.meeron.domain.repository.MeetingRepository
import fourtune.merron.data.model.dto.request.AgendaRequest
import fourtune.merron.data.model.dto.request.MeetingRequest
import fourtune.merron.data.model.dto.request.WorkSpaceUserIdsRequest
import fourtune.merron.data.source.remote.MeetingApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val meetingApi: MeetingApi,
    private val fileProvider: FileProvider,
) : MeetingRepository {
    override suspend fun createMeeting(workSpaceId: Long, meeting: Meeting): Long {
        val (start, end) = meeting.time.split("~")
        val meetingRequest = MeetingRequest(
            workspaceId = workSpaceId,
            meetingDate = meeting.date.formattedString(),
            startTime = start.trim(),
            endTime = end.trim(),
            meetingName = meeting.title,
            meetingPurpose = meeting.purpose,
            operationTeamId = meeting.team.id,
            meetingAdminIds = meeting.ownerIds
        )

        val response = meetingApi.createMeeting(meetingRequest)
        return response.body()?.meetingId ?: throw IOException(response.message())
    }

    override suspend fun addParticipants(meetingId: Long, meeting: Meeting) {
        meetingApi.addParticipants(
            meetingId = meetingId,
            workspaceUserIds = WorkSpaceUserIdsRequest(meeting.participants.map { it.workspaceUserId } - meeting.ownerIds)
        )
    }

    override suspend fun addAgenda(
        meetingId: Long,
        meeting: Meeting
    ) = meetingApi.addAgendas(meetingId = meetingId, AgendaRequest(meeting.agenda)).createdAgendaIds

    override suspend fun addFiles(
        agendaId: Long,
        fileInfo: FileInfo
    ) {
        val pathname = fileProvider.getPath(fileInfo.uriString)
        val mediaType = fileProvider.getMediaType(fileInfo.uriString)
        meetingApi.addFile(
            agendaId = agendaId,
            files = MultipartBody.Part.createFormData(
                name = "files",
                filename = fileInfo.fileName,
                body = File(pathname).asRequestBody("$mediaType/*".toMediaType())
            )
        )
    }

    override suspend fun getTodayMeetings(workSpaceId: Long, workSpaceUserId: Long): List<Meeting> {
        return meetingApi.getTodayMeeting(workSpaceId, workSpaceUserId).meetings.map {
            val (year, month, day) = it.meetingDate.split("/")
            Meeting(
                meetingId = it.meetingId,
                title = it.meetingName,
                date = Date(year.toInt(), month.toInt(), day.toInt()),
                time = "${it.startTime} ~ ${it.endTime}",
                team = Team(it.operationTeamId, it.operationTeamName),
                mainAgendaId = it.mainAgendaId,
                mainAgenda = it.mainAgenda,
                attends = it.attends,
                absents = it.absents,
                unknowns = it.unknowns,
            )
        }
    }

    override suspend fun getYearMeetingCount(type: CalendarType, id: Long): List<YearCount> {
        return meetingApi.getYearMeetingCount(type.name, id)
            .yearCountResponses
            .map { YearCount(it.year, it.count) }
    }

    override suspend fun getMonthMeetingCount(type: CalendarType, id: Long, year: Int): List<MonthCount> {
        return meetingApi.getMonthMeetingCount(type.name, id, year)
            .monthCountResponses
            .map { MonthCount(it.month, it.count) }
    }

    override suspend fun getDateMeetingCount(type: CalendarType, id: Long, date: Date): List<Int> {
        return meetingApi.getDateMeetingCounts(type.name, id, "${date.year}/${date.month}").days
    }

    override suspend fun getDateMeeting(type: CalendarType, id: Long, date: Date): List<Pair<Meeting, WorkSpaceInfo?>> {
        return meetingApi.getDateMeeting(
            type.name,
            id,
            "${date.year}/${date.month}/${date.hourOfDay}"
        ).meetings
            .map { resp ->
                with(resp) {
                    val meeting = Meeting(
                        meetingId = id,
                        title = meetingName,
                        time = "$startTime ~ $endTime",
                    )
                    val workSpaceInfo = if (workspaceId != null && workspaceName != null) WorkSpaceInfo(
                        workspaceId,
                        workspaceName
                    ) else null
                    meeting to workSpaceInfo
                }
            }
    }

    override suspend fun getTeamStates(meetingId: Long): List<TeamState> {
        return meetingApi.getTeamState(meetingId).teamStates
    }
}