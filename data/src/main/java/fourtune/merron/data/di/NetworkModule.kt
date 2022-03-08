package fourtune.merron.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fourtune.merron.data.source.remote.LoginApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .build()

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideConverter(json: Json): Converter.Factory = json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        convertFactory: Converter.Factory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://dev.meeron.net/")
        .addConverterFactory(convertFactory)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideLoginApi(
        retrofit: Retrofit,
    ): LoginApi = retrofit.create(LoginApi::class.java)
}