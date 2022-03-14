package forutune.meeron.domain.preference

import forutune.meeron.domain.model.Token

interface MeeronPreference {
    fun getAccessToken(): String
    fun getRefreshToken(): String
    fun saveToken(token: Token)

    fun saveMyId(id: Long)
    fun getMyId(): Long
}