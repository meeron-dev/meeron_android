package fourtune.merron.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import forutune.meeron.domain.repository.LoginRepository
import forutune.meeron.domain.repository.MeetingRepository
import fourtune.merron.data.repository.LoginRepositoryImpl
import fourtune.merron.data.repository.MeetingRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    fun bindMeetingRepository(meetingRepository: MeetingRepositoryImpl): MeetingRepository
}