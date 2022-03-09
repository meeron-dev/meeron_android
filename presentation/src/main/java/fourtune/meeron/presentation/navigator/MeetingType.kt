package fourtune.meeron.presentation.navigator

import android.os.Bundle
import androidx.navigation.NavType
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MeetingType : NavType<Meeting>(isNullableAllowed = false) {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    override fun get(bundle: Bundle, key: String): Meeting? {
        return bundle.getSerializable(Const.Meeting) as Meeting?
    }

    override fun parseValue(value: String): Meeting {
        return json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Meeting) {
        bundle.putSerializable(key, value)
    }

}
