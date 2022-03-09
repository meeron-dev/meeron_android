package fourtune.merron.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import forutune.meeron.domain.MeeronPreference
import fourtune.merron.data.source.local.preference.Preference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PreferenceModule {
    @Binds
    @Singleton
    fun bindsPreference(preference: Preference): MeeronPreference
}