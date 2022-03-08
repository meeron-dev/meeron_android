package fourtune.merron.data.repository

import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.repository.MeetingRepository
import fourtune.merron.data.model.entity.MeetingEntity
import fourtune.merron.data.source.local.dao.MeetingDao
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val meetingDao: MeetingDao
) : MeetingRepository {
    override suspend fun createMeeting(meeting: Meeting): Long {
        return meetingDao.insert(MeetingEntity.from(meeting))
    }

    override suspend fun getMeeting(meetingId: Long): Meeting {
        return meetingDao.getMeeting(meetingId).toMeeting()
    }

}