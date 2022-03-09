package fourtune.merron.data.source.remote

import forutune.meeron.domain.model.Token
import fourtune.merron.data.model.dto.LoginUserDto
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {
    @Headers("content-type: application/json")
    @POST("/api/login")
    suspend fun login(@Body loginUser: LoginUserDto): Token

    @POST("/api/logout")
    suspend fun logout()
}