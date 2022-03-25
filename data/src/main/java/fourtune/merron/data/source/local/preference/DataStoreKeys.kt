package fourtune.merron.data.source.local.preference

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    object Account {
        val isFirstVisitor = booleanPreferencesKey("isFirstVisitor")
    }

    object Token {
        val accessToken = stringPreferencesKey("accessToken")
        val refreshToken = stringPreferencesKey("refreshToken")
    }

    object Workspace {
        val userId = longPreferencesKey("workSpaceUserId")
        val id = longPreferencesKey("workSpaceId")
    }

    object User {
        val id = longPreferencesKey("userId")
        val name = stringPreferencesKey("userName")
    }
}