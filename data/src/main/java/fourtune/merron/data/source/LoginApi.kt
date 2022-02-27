package fourtune.merron.data.source

import fourtune.merron.data.dto.LoginUserDto
import fourtune.merron.data.dto.Token
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