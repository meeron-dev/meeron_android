package fourtune.merron.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import forutune.meeron.domain.MeeronPreference
import forutune.meeron.domain.di.OK_HTTP_CLIENT
import forutune.meeron.domain.di.OK_HTTP_CLIENT_NO_AUTH
import fourtune.merron.data.source.remote.AuthorizationInterceptor
import fourtune.merron.data.source.remote.LoginApi
import fourtune.merron.data.source.remote.MeetingApi
import fourtune.merron.data.source.remote.TeamApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
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
    fun provideAuthorizationInterceptor(
        meeronPreference: MeeronPreference
    ): Interceptor {
        return AuthorizationInterceptor(meeronPreference)
    }

    @Provides
    @Singleton
    @OK_HTTP_CLIENT
    fun provideOkhttpClient(
        authorizationInterceptor: AuthorizationInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .build()


    @Provides
    @Singleton
    @OK_HTTP_CLIENT_NO_AUTH
    fun provideOkhttpClientNoAuth(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        )
        .build()

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideConverter(json: Json): Converter.Factory = json.asConverterFactory("application/json".toMediaType())

    private fun createRetrofit(convertFactory: Converter.Factory, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(convertFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginApi(
        @OK_HTTP_CLIENT_NO_AUTH okHttpClient: OkHttpClient,
        convertFactory: Converter.Factory,
    ): LoginApi {
        return createRetrofit(convertFactory, okHttpClient).create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTeamApi(
        @OK_HTTP_CLIENT okHttpClient: OkHttpClient,
        convertFactory: Converter.Factory,
    ): TeamApi = createRetrofit(convertFactory, okHttpClient).create(TeamApi::class.java)


    @Provides
    @Singleton
    fun provideMeetingApi(
        @OK_HTTP_CLIENT okHttpClient: OkHttpClient,
        convertFactory: Converter.Factory,
    ): MeetingApi = createRetrofit(convertFactory, okHttpClient).create(MeetingApi::class.java)


    companion object {
        private const val BASE_URL = "https://dev.meeron.net/"
    }
}