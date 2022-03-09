package fourtune.merron.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fourtune.merron.data.model.entity.MeetingEntity
import fourtune.merron.data.model.entity.UserEntity
import fourtune.merron.data.source.local.dao.MeetingDao
import fourtune.merron.data.source.local.dao.UserDao

@Database(
    entities = [MeetingEntity::class, UserEntity::class],
    version = 1
)
@TypeConverters(DataBaseConverter::class)
abstract class DataBase : RoomDatabase() {
    abstract fun meetingDao(): MeetingDao
    abstract fun userDao(): UserDao
}