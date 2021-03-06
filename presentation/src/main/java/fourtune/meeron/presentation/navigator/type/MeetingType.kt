package fourtune.meeron.presentation.navigator.type

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavType
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import fourtune.meeron.presentation.di.TypeInjector
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MeetingType constructor(context: Context) : NavType<Meeting>(isNullableAllowed = false) {
    @Inject
    lateinit var json: Json

    init {
        TypeInjector.invoke(context).inject(this)
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
