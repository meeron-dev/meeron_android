package fourtune.meeron.presentation.navigator.type

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavType
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Team
import fourtune.meeron.presentation.di.TypeInjector
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class TeamType constructor(context: Context) : NavType<Team>(isNullableAllowed = false) {
    @Inject
    lateinit var json: Json

    init {
        TypeInjector.invoke(context).inject(this)
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