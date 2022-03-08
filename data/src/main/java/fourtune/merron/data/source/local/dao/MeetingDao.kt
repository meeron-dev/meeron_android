package fourtune.merron.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import fourtune.merron.data.model.entity.MeetingEntity

@Dao
interface MeetingDao : BaseDao<MeetingEntity> {
    @Query("SELECT * FROM MeetingEntity WHERE id = :meetingId")
    suspend fun getMeeting(meetingId: Long): MeetingEntity
}