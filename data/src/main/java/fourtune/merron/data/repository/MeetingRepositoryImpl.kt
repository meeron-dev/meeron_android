package fourtune.merron.data.repository

import forutune.meeron.domain.FileProvider
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.repository.MeetingRepository
import fourtune.merron.data.model.dto.AgendaDto
import fourtune.merron.data.model.dto.MeetingDto
import fourtune.merron.data.model.dto.request.WorkSpaceUserIds
import fourtune.merron.data.source.local.dao.MeetingDao
import fourtune.merron.data.source.remote.MeetingApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val meetingDao: MeetingDao,
    private val meetingApi: MeetingApi,
    private val fileProvider: FileProvider
) : MeetingRepository {
    override suspend fun createMeeting(meeting: Meeting) {
        val (start, end) = meeting.time.split("~")
        val meetingDto = MeetingDto(
            meetingDate = meeting.date.formattedString(),
            startTime = start.trim(),
            endTime = end.trim(),
            meetingName = meeting.title,
            meetingPurpose = meeting.purpose,
            operationTeamId = meeting.team.id,
            meetingAdminIds = meeting.ownerIds
        )

        val response = meetingApi.createMeeting(meetingDto)
        val meetingId = response.body()?.meetingId ?: throw IOException(response.message())
        meetingApi.addParticipants(
            meetingId = meetingId,
            workspaceUserIds = WorkSpaceUserIds(meeting.participants.map { it.workspaceUserId })
        )
        val agendaResponse = meetingApi.addAgendas(meetingId = meetingId, AgendaDto(meeting.agenda))
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

    override suspend fun getMeeting(meetingId: Long): Meeting {
        return meetingDao.getMeeting(meetingId).toMeeting()
    }

}