package forutune.meeron.domain.repository

import forutune.meeron.domain.model.LoginUser
import forutune.meeron.domain.model.Token
import retrofit2.Response

interface LoginRepository {
    suspend fun login(loginUser: LoginUser): Token
    suspend fun logout(token: String, refreshToken: String): Response<Unit>
}