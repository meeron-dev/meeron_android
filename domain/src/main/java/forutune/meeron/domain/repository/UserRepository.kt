package forutune.meeron.domain.repository

import forutune.meeron.domain.model.Token
import forutune.meeron.domain.model.User

interface UserRepository {
    suspend fun getUser(): User
    suspend fun getUser(email: String): User
    suspend fun setUser(user: User, token: Token)
    suspend fun setUserId(userId: Long?)
    suspend fun getUserId(): Long
    suspend fun createUserName(userName: String)
    suspend fun withdrawal()
}