package fourtune.merron.data.source.remote

import forutune.meeron.domain.model.Token
import retrofit2.http.POST

interface OauthApi {
    @POST("/api/reissue")
    fun reissue(refreshToken: String): Token
}