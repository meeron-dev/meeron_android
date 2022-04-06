package fourtune.merron.data.repository

import forutune.meeron.domain.model.*
import forutune.meeron.domain.provider.FileProvider
import forutune.meeron.domain.repository.MeetingRepository
import fourtune.merron.data.model.dto.request.AgendaRequest
import fourtune.merron.data.model.dto.request.ChangeMeetingStateRequest
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
            meetingName = meeting.meetingName,
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
            val (year, month, day) = it.meeting.startDate.split("/")
            Meeting(
                meetingId = it.meeting.meetingId,
                startDate = it.meeting.startDate,
                startTime = it.meeting.startTime,
                meetingName = it.meeting.meetingName,
                purpose = it.meeting.purpose,
                place = it.meeting.place,
                date = Date(year.toInt(), month.toInt(), day.toInt()),
                time = "${it.meeting.startTime} ~ ${it.meeting.endTime}",
                team = it.team,
                ownerIds = it.admins.map { it.workspaceUserId },
                agenda = it.agendas.map { Agenda(it.agendaOrder, it.agendaName) },
                attends = it.attendCount.attend,
                absents = it.attendCount.absent,
                unknowns = it.attendCount.unknown,
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
                        meetingName = meetingName,
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

    override suspend fun getAgenda(meetingId: Long, agendaOrder: Int): Agenda {
        return with(meetingApi.getAgenda(meetingId, agendaOrder)) {
            Agenda(
                order = agendaOrder,
                name = agendaName,
                issues = issues.map { Issue(it.content) },
                fileInfos = files.map { FileInfo(it.fileUrl, it.fileName) }
            )
        }
    }

    override suspend fun changeMeetingState(
        workSpaceUserId: Long,
        meetingId: Long,
        state: MeetingState
    ) {
        meetingApi.changeMeetingState(workSpaceUserId, ChangeMeetingStateRequest(meetingId, state.name.lowercase()))
    }

    override suspend fun getTeamMember(meetingId: Long, teamId: Long): Map<MeetingState, List<WorkspaceUser>> {
        return mutableMapOf<MeetingState, List<WorkspaceUser>>().apply {
            val teamMembers = meetingApi.getTeamMember(meetingId, teamId)
            put(MeetingState.Attends, teamMembers.attends)
            put(MeetingState.Absents, teamMembers.absents)
            put(MeetingState.Unknowns, teamMembers.unknowns)
        }
    }
}