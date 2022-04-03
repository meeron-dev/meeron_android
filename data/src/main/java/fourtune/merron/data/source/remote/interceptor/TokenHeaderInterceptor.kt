package fourtune.merron.data.source.remote.interceptor

import forutune.meeron.domain.repository.TokenRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.*
import javax.inject.Inject

private const val KEY_AUTHORIZATION = "Authorization"

class TokenHeaderInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request().newBuilder().apply {
            val token = runBlocking { tokenRepository.getAccessToken().firstOrNull() }
            if (!token.isNullOrBlank()) {
                header(KEY_AUTHORIZATION, "Bearer $token")
            }
        }.build().let { request -> chain.proceed(request) }
    }
}

class TokenAuthenticator @Inject constructor(
    private val tokenRepository: TokenRepository
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.header(KEY_AUTHORIZATION).isNullOrEmpty()) return null
        if (response.code == 401) {
            runBlocking { tokenRepository.getAccessToken().firstOrNull() } ?: return null
            runBlocking {
                val token = tokenRepository.reissue()
                tokenRepository.saveToken(token)
            }
        }
        val newToken = runBlocking { tokenRepository.getAccessToken().firstOrNull() } ?: return null

        return response.request
            .newBuilder()
            .removeHeader(KEY_AUTHORIZATION)
            .addHeader(KEY_AUTHORIZATION, "Bearer $newToken")
            .build()
    }
}