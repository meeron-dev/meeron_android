package fourtune.merron.data.source.remote

import forutune.meeron.domain.MeeronPreference
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val pref: MeeronPreference
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
            .newBuilder()
            .addHeader(KEY_AUTHORIZATION, "Bearer ${pref.getAccessToken()}")
            .build()

        return chain.proceed(request)
    }

    companion object {
        const val KEY_AUTHORIZATION = "Authorization"
    }
}