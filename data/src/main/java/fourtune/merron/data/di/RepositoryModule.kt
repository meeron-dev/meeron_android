package fourtune.merron.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import forutune.meeron.domain.repository.*
import fourtune.merron.data.repository.*

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    fun bindMeetingRepository(meetingRepository: MeetingRepositoryImpl): MeetingRepository

    @Binds
    fun bindTeamRepository(teamRepository: TeamRepositoryImpl): TeamRepository

    @Binds
    fun bindUserRepository(userRepository: UserRepositoryImpl): UserRepository

    @Binds
    fun tokenRepository(tokenRepository: TokenRepositoryImpl) : TokenRepository
}