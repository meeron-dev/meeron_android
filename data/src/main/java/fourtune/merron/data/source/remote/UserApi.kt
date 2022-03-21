package fourtune.merron.data.source.remote

import forutune.meeron.domain.model.User
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UserApi {
    @GET("api/users/me")
    suspend fun getMe(): User

    @PATCH("/api/users/{name}")
    suspend fun saveUserName(@Path("name") userName: String)
}