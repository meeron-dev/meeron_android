package fourtune.merron.data.source.remote

import forutune.meeron.domain.model.Token
import retrofit2.http.Header
import retrofit2.http.POST

interface OauthApi {
    @POST("/api/reissue")
    suspend fun reissue(@Header("Authorization") refreshToken: String): Token
}