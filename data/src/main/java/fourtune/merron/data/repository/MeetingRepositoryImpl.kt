package fourtune.merron.data.repository

import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.repository.MeetingRepository
import fourtune.merron.data.model.dto.AgendaDto
import fourtune.merron.data.model.dto.MeetingDto
import fourtune.merron.data.source.local.dao.MeetingDao
import fourtune.merron.data.source.remote.MeetingApi
import java.io.IOException
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val meetingDao: MeetingDao,
    private val meetingApi: MeetingApi
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
        val meetingId = response.body() ?: throw IOException(response.message())
        meetingApi.addParticipants(meetingId = meetingId, meeting.participants.map { it.workspaceUserId })
        meetingApi.addAgendas(meetingId = meetingId, AgendaDto(meeting.agenda))
//        meeting.agenda.forEach {
//            it.files.forEach {
//                meetingApi.addFile(meetingId = meetingId, MultipartBody.Part.createFormData(
//                    name = "files",
//                    filename =it.path,
//                    body =
//                ))
//
//            }
//        }
    }

    override suspend fun getMeeting(meetingId: Long): Meeting {
        return meetingDao.getMeeting(meetingId).toMeeting()
    }

}