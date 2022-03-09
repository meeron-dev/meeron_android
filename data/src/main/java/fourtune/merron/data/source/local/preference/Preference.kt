package fourtune.merron.data.source.local.preference

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import forutune.meeron.domain.MeeronPreference
import forutune.meeron.domain.model.Token
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

    companion object {
        const val MEERON_PREF = "meeron_pref"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }
}