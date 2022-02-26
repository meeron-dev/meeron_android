package forutune.meeron.domain.repository

import forutune.meeron.domain.LoginUser

interface LoginRepository {
    suspend fun login(loginUser: LoginUser)
    suspend fun logout()
}