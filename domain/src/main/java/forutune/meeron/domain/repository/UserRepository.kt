package forutune.meeron.domain.repository

import forutune.meeron.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(): User

    suspend fun setUserId(userId: Long)
    fun getUserId(): Flow<Long?>
    suspend fun createUserName(userName: String)
}