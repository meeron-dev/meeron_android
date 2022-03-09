package forutune.meeron.domain

import forutune.meeron.domain.model.Token

interface MeeronPreference {
    fun getAccessToken(): String
    fun getRefreshToken(): String
    fun saveToken(token: Token)
}