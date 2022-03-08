package fourtune.merron.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fourtune.merron.data.source.local.DataBase
import fourtune.merron.data.source.local.dao.MeetingDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): DataBase =
        Room.databaseBuilder(context, DataBase::class.java, "meeron.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideMeetingDao(dataBase: DataBase): MeetingDao = dataBase.meetingDao()
}