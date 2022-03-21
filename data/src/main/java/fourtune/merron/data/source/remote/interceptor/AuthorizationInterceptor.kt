package fourtune.merron.data.source.remote.interceptor

import forutune.meeron.domain.repository.TokenRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenRepository.getAccessToken().firstOrNull() }

        val request = chain.request()
            .newBuilder()
            .addHeader(KEY_AUTHORIZATION, "Bearer $token")
            .build()

        return chain.proceed(request)
    }

    companion object {
        const val KEY_AUTHORIZATION = "Authorization"
    }
}