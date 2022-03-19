package fourtune.meeron.presentation.navigator

import android.os.Bundle
import androidx.navigation.NavType
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Date
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class DateType : NavType<Date>(isNullableAllowed = false) {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    override fun get(bundle: Bundle, key: String): Date? {
        return bundle.getSerializable(Const.Date) as Date?
    }

    override fun parseValue(value: String): Date {
        return json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Date) {
        bundle.putSerializable(key, value)
    }

}