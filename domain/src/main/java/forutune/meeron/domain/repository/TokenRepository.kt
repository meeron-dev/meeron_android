package forutune.meeron.domain.repository

import forutune.meeron.domain.model.Token
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun getAccessToken(): Flow<String?>
    fun getRefreshToken(): Flow<String?>
    suspend fun saveToken(token: Token)
    suspend fun clearToken()
    suspend fun reissue(): Token
}