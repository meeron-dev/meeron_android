package fourtune.merron.data.repository

import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.repository.MeetingRepository
import fourtune.merron.data.model.dto.MeetingDto
import fourtune.merron.data.source.local.dao.MeetingDao
import fourtune.merron.data.source.remote.MeetingApi
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

        meetingApi.createMeeting(meetingDto)
    }

    override suspend fun getMeeting(meetingId: Long): Meeting {
        return meetingDao.getMeeting(meetingId).toMeeting()
    }

}