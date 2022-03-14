package fourtune.merron.data.source.local.preference

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import forutune.meeron.domain.model.Token
import forutune.meeron.domain.preference.MeeronPreference
import javax.inject.Inject

class Preference @Inject constructor(@ApplicationContext context: Context) : MeeronPreference {
    private val preference = context.getSharedPreferences(MEERON_PREF, Context.MODE_PRIVATE)

    override fun getAccessToken(): String {
        return preference.getString(ACCESS_TOKEN, "") ?: ""
    }

    override fun getRefreshToken(): String {
        return preference.getString(REFRESH_TOKEN, "") ?: ""
    }

    override fun saveToken(token: Token) {
        with(preference.edit()) {
            putString(ACCESS_TOKEN, token.accessToken)
            putString(REFRESH_TOKEN, token.refreshToken)
            commit()
        }
    }

    override fun saveMyId(id: Long) {
        with(preference.edit()) {
            putLong(MY_ID, id)
            commit()
        }
    }

    override fun getMyId(): Long {
        return preference.getLong(MY_ID, -1)
    }

    override fun setCurrentWorkSpace(workSpaceId: Long) {
        with(preference.edit()) {
            putLong(WORKSPACE_ID, workSpaceId)
            commit()
        }
    }

    override fun getCurrentWorkSpace(): Long {
        return preference.getLong(WORKSPACE_ID, -1)
    }

    override fun setCurrentWorkSpaceUserId(workSpaceUserId: Long) {
        with(preference.edit()) {
            putLong(WORKSPACE_USER_ID, workSpaceUserId)
            commit()
        }
    }

    override fun getCurrentWorkSpaceUserId(): Long {
        return preference.getLong(WORKSPACE_USER_ID, -1)
    }

    companion object {
        const val MEERON_PREF = "meeron_pref"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val MY_ID = "my_id"
        const val WORKSPACE_USER_ID = "workspace_user_id"
        const val WORKSPACE_ID = "workspace_id"
    }
}