package fourtune.merron.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import forutune.meeron.domain.model.Token
import forutune.meeron.domain.repository.TokenRepository
import fourtune.merron.data.source.local.preference.DataStoreKeys
import fourtune.merron.data.source.remote.OauthApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val oauthApi: OauthApi
) : TokenRepository {
    override fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { it[DataStoreKeys.Token.accessToken] }
    }

    override fun getRefreshToken(): Flow<String?> {
        return dataStore.data.map { it[DataStoreKeys.Token.refreshToken] }
    }

    override suspend fun saveToken(token: Token) {
        dataStore.edit {
            it[DataStoreKeys.Token.accessToken] = token.accessToken
            it[DataStoreKeys.Token.refreshToken] = token.refreshToken
        }
    }

    override suspend fun clearToken() {
        dataStore.edit {
            it.remove(DataStoreKeys.Token.accessToken)
            it.remove(DataStoreKeys.Token.refreshToken)
        }
    }

    override suspend fun reissue(): Token {
        val token = requireNotNull(getRefreshToken().firstOrNull())
        return oauthApi.reissue("Bearer $token")
    }
}