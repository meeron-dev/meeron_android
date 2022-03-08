package forutune.meeron.domain.repository

import forutune.meeron.domain.model.Meeting

interface MeetingRepository {
    suspend fun createMeeting(meeting: Meeting): Long
    suspend fun getMeeting(meetingId: Long): Meeting
}