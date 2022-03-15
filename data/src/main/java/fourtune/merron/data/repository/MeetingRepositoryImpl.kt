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
    override suspend fun createMeeting(workSpaceId: Long, meeting: Meeting) {
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
        val meetingId = response.body()?.meetingId ?: throw IOException(response.message())
        meetingApi.addParticipants(
            meetingId = meetingId,
            workspaceUserIds = WorkSpaceUserIdsRequest(meeting.participants.map { it.workspaceUserId })
        )
        val agendaResponse = meetingApi.addAgendas(meetingId = meetingId, AgendaRequest(meeting.agenda))
        meeting.agenda.forEachIndexed { index, agenda ->
            agenda.fileInfos.forEach { fileInfo ->
                val pathname = fileProvider.getPath(fileInfo.uriString)
                val mediaType = fileProvider.getMediaType(fileInfo.uriString)
                meetingApi.addFile(
                    agendaId = agendaResponse.agendaResponses[index].createdAgendaId, MultipartBody.Part.createFormData(
                        name = "files",
                        filename = fileInfo.fileName,
                        body = File(pathname).asRequestBody("$mediaType/*".toMediaType())
                    )
                )
            }
        }
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
                status = MeetingStatus.getStatus(it.meetingStatus)
            )
        }
    }

    override suspend fun getYearMeetingCount(type: CalendarType, id: Long): List<YearCount> {
        return meetingApi.getYearMeetingCount(type.name, id).yearCountResponses.map { YearCount(it.year, it.count) }
    }

}