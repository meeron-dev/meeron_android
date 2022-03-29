package fourtune.meeron.presentation.navigator.type

import android.os.Bundle
import androidx.navigation.NavType
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Team
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class TeamType : NavType<Team>(isNullableAllowed = false) {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    override fun get(bundle: Bundle, key: String): Team? {
        return bundle.getSerializable(Const.Team) as Team?
    }

    override fun parseValue(value: String): Team {
        return json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Team) {
        bundle.putSerializable(key, value)
    }

}