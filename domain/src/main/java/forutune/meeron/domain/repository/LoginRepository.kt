package forutune.meeron.domain.repository

import forutune.meeron.domain.model.LoginUser
import forutune.meeron.domain.model.Token

interface LoginRepository {
    suspend fun login(loginUser: LoginUser): Token
    suspend fun logout()
}