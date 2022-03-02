package fourtune.meeron.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideCalendar(): Calendar = Calendar.getInstance()
}