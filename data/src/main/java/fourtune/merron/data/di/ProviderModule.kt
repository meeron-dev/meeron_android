package fourtune.merron.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import forutune.meeron.domain.FileProvider
import fourtune.merron.data.source.local.FileProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ProviderModule {
    @Binds
    @Singleton
    fun bindFileProvider(fileProviderImpl: FileProviderImpl): FileProvider
}