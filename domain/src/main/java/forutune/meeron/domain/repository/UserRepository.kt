package forutune.meeron.domain.repository

import forutune.meeron.domain.model.Token
import forutune.meeron.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(): User
    suspend fun getUser(email: String): User?
    suspend fun setUserId(userId: Long)
    fun getUserId(): Flow<Long?>
    suspend fun createUserName(userName: String)
    suspend fun setUser(user: User, token: Token)
    suspend fun updateToken(user: User, token: Token)
}