package fourtune.merron.data.source.local.preference

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    object Account{
        val isFirstVisitor = booleanPreferencesKey("isFirstVisitor")
    }

    object Token {
        val accessToken = stringPreferencesKey("accessToken")
        val refreshToken = stringPreferencesKey("refreshToken")
    }

    object User {
        val id = longPreferencesKey("userId")
        val workSpaceUserId = longPreferencesKey("workSpaceUserId")
    }
}