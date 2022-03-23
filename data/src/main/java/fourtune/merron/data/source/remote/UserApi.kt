package fourtune.merron.data.source.remote

import forutune.meeron.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApi {
    @GET("api/users/me")
    suspend fun getMe(): User

    @PATCH("/api/users/name")
    suspend fun saveUserName(@Body name: String): Response<Unit>
}