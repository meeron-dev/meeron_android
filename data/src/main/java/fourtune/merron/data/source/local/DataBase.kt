package fourtune.merron.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import fourtune.merron.data.model.entity.MeetingEntity
import fourtune.merron.data.source.local.dao.MeetingDao

@Database(
    entities = [MeetingEntity::class],
    version = 1
)
abstract class DataBase : RoomDatabase() {
    abstract fun meetingDao(): MeetingDao
}